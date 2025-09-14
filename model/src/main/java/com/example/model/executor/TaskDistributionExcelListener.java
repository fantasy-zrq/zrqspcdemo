package com.example.model.executor;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.model.dto.req.CouponTaskExcelObject;
import com.example.model.entity.CouponDO;
import com.example.model.entity.CouponDistributionFailDO;
import com.example.model.entity.ReceiveDO;
import com.example.model.entity.TaskDO;
import com.example.model.entity.mapper.CouponDistributionFailMapper;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.entity.mapper.ReceiveMapper;
import com.example.model.entity.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;

import static com.example.model.common.constance.RedisConstanceKey.*;

/**
 * @author zrq
 * @time 2025/9/10 20:11
 * @description
 */
@Slf4j(topic = "TaskDistributionExcelListener")
@RequiredArgsConstructor
public class TaskDistributionExcelListener extends AnalysisEventListener<CouponTaskExcelObject> {

    private final StringRedisTemplate stringRedisTemplate;
    private final TaskMapper taskMapper;
    private final ReceiveMapper receiveMapper;
    private final CouponMapper couponMapper;
    private final CouponDO couponDO;
    private final TaskDO taskDO;
    private final CouponDistributionFailMapper couponDistributionFailMapper;
    private final Map<ReceiveDO, Long> receiveCache = new HashMap<>(512);
    private final List<String> receiveKeyCache = new ArrayList<>(512);
    private final List<String> redisArgsCache = new ArrayList<>(1024);
    private final Integer batchSize = 500;
    private final String luaPath = "lua/coupon_batch_distribution_script.lua";
    private final Integer taskDistributionRowKey;

    @Override
    public void invoke(CouponTaskExcelObject couponTaskExcelObject, AnalysisContext analysisContext) {
        //假如断电宕机了一次
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(String.format(REDIS_COUPON_DISTRIBUTION_KEY, taskDO.getTaskId(), couponDO.getCouponId())))
                && analysisContext.readRowHolder().getRowIndex() <= taskDistributionRowKey) {
            log.info("该条记录已经被消费过了，还没到达宕机前的消费点位");
            return;
        }
        //下一次补充优惠券库存，那么这个任务需要从上一次的执行位置继续往下执行
        if ((taskDO.getSendNum() + taskDistributionRowKey) < analysisContext.readRowHolder().getRowIndex()) {
            log.warn("已经达到优惠券分发的数量上限--->分发数量：[{}]", taskDO.getSendNum());
            return;
        }
        if (Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForHash().get(String.format(REDIS_COUPON_CREATE_KEY, taskDO.getCouponId()), "couponStock")).toString()) < 1) {
            log.info("redis中优惠券--【{}】--库存不足", taskDO.getCouponId());
            return;
        }
        //TODO--记录消息的执行位置
        stringRedisTemplate.opsForHash().put(
                String.format(REDIS_COUPON_DISTRIBUTION_KEY, taskDO.getTaskId(), couponDO.getCouponId()),
                "taskDistributionRowKey",
                String.valueOf(analysisContext.readRowHolder().getRowIndex())
        );
        //代表该张优惠券当前用户在redis中已经有记录了
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(String.format(REDIS_TASK_DISTRIBUTION_KEY, couponDO.getCouponId(), couponTaskExcelObject.getUserId())))) {
            return;
        }
        stringRedisTemplate.opsForHash().increment(String.format(REDIS_COUPON_CREATE_KEY, taskDO.getCouponId()), "couponStock", -1);
        int res = couponMapper.decrementCouponStockByCouponId(couponDO.getCouponId());
        if (res < 1) {
            return;
        }

        ReceiveDO receiveDO = ReceiveDO.builder()
                .id(IdWorker.getId())
                .userId(Long.parseLong(couponTaskExcelObject.getUserId()))
                .couponId(couponDO.getCouponId())
                .receiveNumber(1)
                .receiveTime(new Date())
                .startTime(couponDO.getStartTime())
                .endTime(couponDO.getEndTime())
                .status(0)
                .build();
        receiveCache.put(receiveDO, (long) DateUtil.millisecond(receiveDO.getEndTime()));
        receiveKeyCache.add(String.format(REDIS_TASK_DISTRIBUTION_KEY, couponDO.getCouponId(), couponTaskExcelObject.getUserId()));
        if (receiveCache.size() >= batchSize) {
            doBatchInsert();
        }
    }

    //TODO--批处理插入.
    private void doBatchInsert() {
        if (receiveCache.isEmpty() || receiveKeyCache.isEmpty()) {
            return;
        }
        //先插入MySQL
        try {
            //只要有一条语句失败，那么改为串行化
            receiveMapper.batchInsert(new ArrayList<>(receiveCache.keySet()));
        } catch (Exception e) {
            log.error("mysql-batch-insert-error---[{}]", e.getMessage());
            log.info("开始串行化执行insert语句");
            for (ReceiveDO receiveDO : receiveCache.keySet()) {
                try {
                    receiveMapper.insert(receiveDO);
                } catch (Exception ex) {
                    log.error("串行化插入记录失败--->[{}],进行失败日志的记录", JSON.toJSONString(receiveDO));
                    JSONObject json = new JSONObject();
                    json.put("distribution_person", receiveDO.getUserId());
                    json.put("distribution_fail_cause", ex.getMessage());
                    //后续看是否通过xxl-job来定时读取失败日志，重发优惠券
                    CouponDistributionFailDO failDO = CouponDistributionFailDO.builder()
                            .taskId(taskDO.getTaskId())
                            .failJson(json.toJSONString())
                            .build();
                    couponDistributionFailMapper.insert(failDO);
                }
            }
        }
        //再插入Redis
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource(luaPath));
        script.setResultType(List.class);
        receiveCache.forEach((key, value) -> {
            redisArgsCache.add(JSONObject.toJSONString(key));
            redisArgsCache.add(String.valueOf(value));
        });
        //java端无法通过捕获异常的方式来处理redis的异常插入问题
        List redisFailList = stringRedisTemplate.execute(script, receiveKeyCache, redisArgsCache.toArray());
        if (Objects.nonNull(redisFailList) && !redisFailList.isEmpty()) {
            for (Object receiveDoJsonStr : redisFailList) {
                log.error("redis中已经存在该条领取记录----【{}】", receiveDoJsonStr);
            }
        }
        receiveCache.clear();
        receiveKeyCache.clear();
        redisArgsCache.clear();
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //假如有789条excel记录，按照500次分批，那么这里最终会执行完剩下的289条记录
        doBatchInsert();
        TaskDO task = TaskDO.builder()
                .taskId(taskDO.getTaskId())
                .taskStatus(3)
                .taskCompleteTime(new Date())
                .build();
        taskMapper.updateById(task);
        log.info("所有用户优惠券都已经领取完成");
    }
}
