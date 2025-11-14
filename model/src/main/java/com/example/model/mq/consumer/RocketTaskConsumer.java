package com.example.model.mq.consumer;

import com.alibaba.excel.EasyExcel;
import com.example.model.common.exception.ClientException;
import com.example.model.dto.req.CouponTaskExcelObject;
import com.example.model.entity.CouponDO;
import com.example.model.entity.TaskDO;
import com.example.model.entity.mapper.CouponDistributionFailMapper;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.executor.TaskDistributionExcelListener;
import com.example.model.mq.producer.RocketMqCouponBatchDistributionProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_COUPON_CREATE_KEY;
import static com.example.model.common.enums.CouponEnableStatusEnum.END;


/**
 * @author zrq
 * @time 2025/9/10 17:12
 * @description
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "RocketTaskConsumer")
@RocketMQMessageListener(consumerGroup = "zrq-spc-task-excel-consumer-group", topic = "zrq-spc-task-excel-topic")
public class RocketTaskConsumer implements RocketMQListener<TaskDO> {

    private final StringRedisTemplate stringRedisTemplate;
    private final CouponMapper couponMapper;
    private final CouponDistributionFailMapper couponDistributionFailMapper;
    private final RocketMqCouponBatchDistributionProducer rocketMqCouponBatchDistributionProducer;
//    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onMessage(TaskDO message) {
        //TODO-->写幂等接口
        log.info("RocketTaskConsumer 接收到消息-->{}", message.toString());
        CouponDO couponDO = couponMapper.selectById(message.getCouponId());
        if (Objects.equals(stringRedisTemplate.hasKey(String.format(REDIS_COUPON_CREATE_KEY, message.getCouponId())), Boolean.FALSE)) {
            throw new ClientException("redis中对应coupon_id的记录----->" + message.getCouponId());
        }
        if (couponDO.getCouponStatus().equals(END.status)) {
            log.error("优惠券--【{}】--已经终止", couponDO);
            return;
        }
        TaskDistributionExcelListener listener = new TaskDistributionExcelListener(
                stringRedisTemplate,
                couponDO,
                message,
                couponDistributionFailMapper,
                rocketMqCouponBatchDistributionProducer
        );
        EasyExcel.read(message.getFileAddress(), CouponTaskExcelObject.class, listener).sheet().doRead();
    }
}
