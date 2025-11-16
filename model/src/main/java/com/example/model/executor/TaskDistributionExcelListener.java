package com.example.model.executor;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.example.model.common.utils.RedisResultParser;
import com.example.model.dto.req.CouponTaskExcelObject;
import com.example.model.entity.CouponBatchDistributionDO;
import com.example.model.entity.CouponDO;
import com.example.model.entity.CouponDistributionFailDO;
import com.example.model.entity.TaskDO;
import com.example.model.entity.mapper.CouponDistributionFailMapper;
import com.example.model.mq.producer.RocketMqCouponBatchDistributionProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;
import java.util.Map;

import static com.example.model.common.constance.RedisConstanceKey.*;

/**
 * @author zrq
 * @time 2025/9/10 20:11
 * @description 重构优惠券分发逻辑
 */
@Slf4j(topic = "TaskDistributionExcelListener")
@RequiredArgsConstructor
public class TaskDistributionExcelListener extends AnalysisEventListener<CouponTaskExcelObject> {

    private final StringRedisTemplate stringRedisTemplate;
    private final CouponDO couponDO;
    private final TaskDO taskDO;
    private final CouponDistributionFailMapper couponDistributionFailMapper;
    private final RocketMqCouponBatchDistributionProducer rocketMqCouponBatchDistributionProducer;
    private final Integer batchSize = 5;
    private final String luaPath = "lua/coupon_batch_distribution_script.lua";
    //这个把rowCount定义为2，直接就是第一个用户所在行
    private Integer rowCount = 2;

    @Override
    public void invoke(CouponTaskExcelObject couponTaskExcelObject, AnalysisContext analysisContext) {
        log.info("rowCount--->{}", rowCount);
        Long taskId = taskDO.getTaskId();
        String distributionProcessKey = String.format(REDIS_COUPON_DISTRIBUTION_PROCESS_KEY, taskId);
        String process = stringRedisTemplate.opsForValue().get(distributionProcessKey);
        //用于跳过前process条已经处理过的记录,这个process肯定是从1开始递增的，不能指代excel的行数

        if (StrUtil.isNotBlank(process) && Integer.parseInt(process) >= rowCount) {
            rowCount++;
            return;
        }

        //用于每次调用时走缓存
        DefaultRedisScript<Long> script = Singleton.get(luaPath, () -> {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(luaPath)));
            redisScript.setResultType(Long.class);
            return redisScript;
        });
        String couponRedisKey = String.format(REDIS_COUPON_CREATE_KEY, couponDO.getCouponId());
        //采用List作为数据结构LPUSH--RPOP
        String couponUserListRedisKey = String.format(REDIS_COUPON_DISTRIBUTION_LIST_KEY, taskId, couponDO.getCouponId());

        //这里构建的map存放的是真实excel用户所在行数
        Map<Object, Object> insertRedisMap = MapBuilder.create()
                .put("userId", couponTaskExcelObject.getUserId())
                .put("rowCount", rowCount)
                .build();
        Long redisExecuteRes = stringRedisTemplate.execute(script, List.of(couponRedisKey, couponUserListRedisKey), JSON.toJSONString(insertRedisMap));
        boolean first = RedisResultParser.parseFirst(redisExecuteRes);
        //库存扣减失败
        if (!first) {
            stringRedisTemplate.opsForValue().set(distributionProcessKey, rowCount.toString());
            Map<Object, Object> failMap = MapBuilder.create()
                    .put("userId", couponTaskExcelObject.getUserId())
                    .put("couponId", couponDO.getCouponId())
                    .put("rowCount", rowCount)
                    .put("reason", "库存不足")
                    .build();
            CouponDistributionFailDO failDO = CouponDistributionFailDO.builder()
                    .taskId(taskDO.getTaskId())
                    .failJson(JSON.toJSONString(failMap))
                    .build();
            couponDistributionFailMapper.insert(failDO);
            rowCount++;
            return;
        }
        Long redisUserListLength = RedisResultParser.parseSecond(redisExecuteRes);
        if (redisUserListLength % batchSize != 0) {
            log.info("批量插入");
            stringRedisTemplate.opsForValue().set(distributionProcessKey, rowCount.toString());
            rowCount++;
            return;
        }

        CouponBatchDistributionDO batchDistributionDO = CouponBatchDistributionDO.builder()
                .taskId(taskDO.getTaskId())
                .taskName(taskDO.getTaskName())
                .couponId(couponDO.getCouponId())
                .startTime(couponDO.getStartTime())
                .endTime(couponDO.getEndTime())
                .sendType(taskDO.getSendType())
                .sendTime(taskDO.getSendTime())
                .validEndTime(couponDO.getEndTime())
                .batchUserSetSize(batchSize)
                .taskStatus(1)
                .lastBatch(false)
                .build();
        log.info("after-batch------------");
        rocketMqCouponBatchDistributionProducer.senMessage(batchDistributionDO);
        stringRedisTemplate.opsForValue().set(distributionProcessKey, rowCount.toString());
        rowCount++;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //这里直接再查一次缓存得出确切的batchUserListSize容量，来计算出最后一批次的个数
        String couponUserListRedisKey = String.format(REDIS_COUPON_DISTRIBUTION_LIST_KEY, taskDO.getTaskId(), couponDO.getCouponId());
        Long batchUserListSize = stringRedisTemplate.opsForList().size(couponUserListRedisKey);
        if (batchUserListSize % batchSize == 0) {
            return;
        }
        log.info("last-batch");
        CouponBatchDistributionDO batchDistributionDO = CouponBatchDistributionDO.builder()
                .taskId(taskDO.getTaskId())
                .taskName(taskDO.getTaskName())
                .couponId(couponDO.getCouponId())
                .startTime(couponDO.getStartTime())
                .endTime(couponDO.getEndTime())
                .sendType(taskDO.getSendType())
                .sendTime(taskDO.getSendTime())
                .validEndTime(couponDO.getEndTime())
                .batchUserSetSize((int) (batchUserListSize % batchSize))
                .taskStatus(1)
                .lastBatch(Boolean.TRUE)
                .build();
        rocketMqCouponBatchDistributionProducer.senMessage(batchDistributionDO);
    }
}
