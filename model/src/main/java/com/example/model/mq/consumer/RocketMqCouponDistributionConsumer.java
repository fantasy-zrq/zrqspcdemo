package com.example.model.mq.consumer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.model.base.MessageWrapper;
import com.example.model.entity.*;
import com.example.model.entity.mapper.CouponDistributionFailMapper;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.entity.mapper.ReceiveMapper;
import com.example.model.entity.mapper.TaskMapper;
import com.example.starter.autoconfig.log.NoMQDuplicateConsume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchExecutorException;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.model.common.constance.RedisConstanceKey.*;

/**
 * @author zrq
 * @time 2025/11/14 21:03
 * @description
 */
@Component
@Slf4j(topic = "RocketMqCouponDistributionConsumer")
@RocketMQMessageListener(
        topic = "zrq-spc-task-excel-topic-batch-distribution-rebuild-A",
        consumerGroup = "zrq-spc-task-excel-topic-batch-distribution-consumer-group-rebuild-A"
)
@RequiredArgsConstructor
public class RocketMqCouponDistributionConsumer implements RocketMQListener<MessageWrapper<CouponBatchDistributionDO>> {

    private final StringRedisTemplate stringRedisTemplate;
    private final ReceiveMapper receiveMapper;
    private final CouponMapper couponMapper;
    private final TaskMapper taskMapper;
    private final CouponDistributionFailMapper couponDistributionFailMapper;
    private static final String LUA_PATH = "lua/coupon_batch_insert_script.lua";

    @NoMQDuplicateConsume(
            keyPrefix = "coupon_task_execute:idempotent:",
            key = "#message.msg.taskId",
            keyTimeout = 60 * 2
    )
    @Override
    public void onMessage(MessageWrapper<CouponBatchDistributionDO> message) {
        CouponBatchDistributionDO distributionDO = message.getMsg();
        if (!distributionDO.getLastBatch()) {
            decrementCouponStock(distributionDO, distributionDO.getBatchUserSetSize());
        } else {
            String couponUserListRedisKey = String.format(REDIS_COUPON_DISTRIBUTION_LIST_KEY, distributionDO.getTaskId(), distributionDO.getCouponId());
            //listener最后已经设置过BatchUserSetSize了
            decrementCouponStock(distributionDO, distributionDO.getBatchUserSetSize());
            //这里是否需要弹出所有的有待讨论，因为解析excel这个消息从消息队列发出被consumer接收以后，是单线程进行解析的，所以这个couponUserListRedisKey只会有一个线程在使用
            //这里弹出所有的剩余集合也能说的过去
            List<String> remaining = stringRedisTemplate.opsForList().rightPop(couponUserListRedisKey, Integer.MAX_VALUE);
            if (CollectionUtil.isNotEmpty(remaining)) {
                List<CouponDistributionFailDO> failList = new ArrayList<>();
                remaining.forEach(each -> {
                    Map map = JSON.parseObject(each, Map.class);
                    CouponDistributionFailDO failDO = CouponDistributionFailDO.builder()
                            .id(IdUtil.getSnowflakeNextId())
                            .taskId(distributionDO.getTaskId())
                            .failJson(JSON.toJSONString(MapUtil.builder()
                                    .put("rowCount", map.get("rowCount").toString())
                                    .put("failed_reason", "优惠券库存不足")
                                    .build()))
                            .build();
                    failList.add(failDO);
                });
                couponDistributionFailMapper.batchInsert(failList);
            }
            TaskDO taskDO = TaskDO.builder()
                    .taskStatus(3)
                    .taskCompleteTime(new Date())
                    .build();
            taskMapper.update(taskDO, Wrappers.lambdaUpdate(TaskDO.class)
                    .eq(TaskDO::getTaskId, distributionDO.getTaskId())
                    .eq(TaskDO::getDelFlag, 0));
        }
    }

    /**
     * 扣减mysql、redis库存，增加失败名单、增加领取名单、redis增加已领取用户集合
     */
    private void decrementCouponStock(CouponBatchDistributionDO distributionDO, Integer batchSize) {
        //这里得到的decrementRes是扣减的MySQL库存数---这里传入的batchSize==5，但实际stock==3，那么这里只会扣减3
        Integer decrementMySQLRes = decrementMySQLCouponStock(distributionDO.getCouponId(), batchSize);
        if (decrementMySQLRes <= 0) {
            log.info("decrementCouponStock方法扣减MySQL库存失败--decrementRes=={}", decrementMySQLRes);
            return;
        }
        String couponUserSetRedisKey = String.format(REDIS_COUPON_DISTRIBUTION_LIST_KEY, distributionDO.getTaskId(), distributionDO.getCouponId());
        //redis用户集合扣减
        List<String> redisCouponUserRightPoPList = stringRedisTemplate.opsForList().rightPop(couponUserSetRedisKey, decrementMySQLRes);
        //增加MySQL的接收表记录
        List<ReceiveDO> receiveList = new ArrayList<>(decrementMySQLRes);
        redisCouponUserRightPoPList.forEach(each -> {
            Map map = JSON.parseObject(each, Map.class);
            ReceiveDO receiveDO = ReceiveDO.builder()
                    .id(IdUtil.getSnowflakeNextId())
                    .userId(Long.parseLong(map.get("userId").toString()))
                    .couponId(distributionDO.getCouponId())
                    .receiveNumber(1)
                    .receiveTime(new Date())
                    .startTime(distributionDO.getStartTime())
                    .endTime(distributionDO.getEndTime())
                    .useTime(null)
                    .status(0)
                    .rowCount(Integer.parseInt(map.get("rowCount").toString()))
                    .build();
            receiveList.add(receiveDO);
        });
        batchInsertCouponReceive(receiveList, distributionDO.getTaskId());
        //添加用户到redis领券集合设置过期时间，限制集合。
        List<String> userIdList = receiveList.stream().map(ReceiveDO::getUserId).map(String::valueOf).toList();
        String userIdJson = JSONObject.toJSONString(userIdList);
        List<String> args = List.of(
                userIdJson,
                String.valueOf(new Date().getTime()),
                String.valueOf(Duration.between(
                        LocalDateTime.now(),
                        distributionDO.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                ).getSeconds())
        );
        List<String> keys = List.of(
                REDIS_COUPON_DISTRIBUTION_LIMIT_KEY,
                REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY.replace("%s", ""),
                distributionDO.getCouponId().toString()
        );
        DefaultRedisScript<Void> luaScript = Singleton.get(LUA_PATH, () -> {
            DefaultRedisScript<Void> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_PATH)));
            redisScript.setResultType(Void.class);
            return redisScript;
        });
        //redis增加用户领取集合ZSET
        stringRedisTemplate.execute(luaScript, keys, args.toArray());
    }

    private void batchInsertCouponReceive(List<ReceiveDO> receiveList, Long taskId) {
        try {
            receiveMapper.batchInsert(receiveList);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof BatchExecutorException) {
                log.error("receiveList批次插入失败");
                List<ReceiveDO> removeList = new ArrayList<>();
                List<CouponDistributionFailDO> failList = new ArrayList<>();
                receiveList.forEach(each -> {
                    try {
                        receiveMapper.insert(each);
                    } catch (Exception ex) {
                        //这里假如已经领取，会被t_receive表的user_id,coupon_id,receive_number组成的唯一索引给拦截
                        //这里不要先查询再插入，因为无法保证查询完以后，准备新增一定用户不存在，其他线程可能修改这个变量，最后还是依赖唯一索引
                        //这种乐观锁的方式比较合理
                        if (ex.getCause() instanceof DuplicateKeyException) {
                            log.error("用户--{}，已经领取过优惠券--将其假如失败队列", each.getUserId());
                            Map<Object, Object> info = MapUtil.builder()
                                    .put("rowCount", each.getRowCount())
                                    .put("failed_reason", "已经领取过优惠券")
                                    .build();
                            CouponDistributionFailDO failDO = CouponDistributionFailDO.builder()
                                    .taskId(taskId)
                                    .failJson(JSON.toJSONString(info))
                                    .build();
                            failList.add(failDO);
                            //这里加入这个removeList用于下面的删除
                            removeList.add(each);
                        }
                        //这里是否需要抛出异常？
                    }
                });
                couponDistributionFailMapper.batchInsert(failList);

                //这里要将单条记录插入失败的给库存还回去，decrementMySQLCouponStock方法已经 扣减了比如5个库存，但是这里单个插入只插入了3个就应该放回两条库存
                int back = receiveList.size() - removeList.size();
                if (back == 0) {
                    return;
                }
                Long couponId = receiveList.get(0).getCouponId();
                couponMapper.updateCouponStock(couponId, removeList.size());
                stringRedisTemplate.opsForHash().increment(String.format(REDIS_COUPON_CREATE_KEY, couponId.toString()), "couponStock", back);
                receiveList.removeAll(removeList);
                return;
            }
            throw e;
        }
    }

    private Integer decrementMySQLCouponStock(Long couponId, Integer batchSize) {
        CouponDO couponDO = couponMapper.selectById(couponId);
        if (couponDO == null) {
            return 0;
        }

        int stock = couponDO.getCouponStock();
        if (stock <= 0) {
            return 0;
        }

        int realDecrement = Math.min(stock, batchSize);

        int update = couponMapper.decrementMySQLCouponStock(couponId, realDecrement);
        return update > 0 ? realDecrement : 0;
    }
}
