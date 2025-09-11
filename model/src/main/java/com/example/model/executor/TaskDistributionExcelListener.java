package com.example.model.executor;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.example.model.dto.req.CouponTaskExcelObject;
import com.example.model.entity.CouponDO;
import com.example.model.entity.ReceiveDO;
import com.example.model.entity.TaskDO;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.entity.mapper.ReceiveMapper;
import com.example.model.entity.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.Objects;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_COUPON_CREATE_KEY;
import static com.example.model.common.constance.RedisConstanceKey.REDIS_TASK_DISTRIBUTION_KEY;

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

    @Override
    public void invoke(CouponTaskExcelObject couponTaskExcelObject, AnalysisContext analysisContext) {

        if (Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForHash().get(String.format(REDIS_COUPON_CREATE_KEY, taskDO.getCouponId()), "couponStock")).toString()) < 1) {
            return;
        }
        stringRedisTemplate.opsForHash().increment(String.format(REDIS_COUPON_CREATE_KEY, taskDO.getCouponId()), "couponStock", -1);
        int res = couponMapper.decrementCouponStockByCouponId(couponDO.getCouponId());
        if (res < 1) {
            return;
        }

        ReceiveDO receiveDO = ReceiveDO.builder()
                .userId(Long.parseLong(couponTaskExcelObject.getUserId()))
                .couponId(couponDO.getCouponId())
                .receiveNumber(1)
                .receiveTime(new Date())
                .startTime(couponDO.getStartTime())
                .endTime(couponDO.getEndTime())
                .status(0)
                .build();
        receiveMapper.insert(receiveDO);

        stringRedisTemplate.opsForValue()
                .set(String.format(REDIS_TASK_DISTRIBUTION_KEY, couponTaskExcelObject.getUserId()), JSON.toJSONString(receiveDO));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        TaskDO task = TaskDO.builder()
                .taskId(taskDO.getTaskId())
                .taskStatus(3)
                .taskCompleteTime(new Date())
                .build();
        taskMapper.updateById(task);
        log.info("所有用户优惠券都已经领取完成");
    }
}
