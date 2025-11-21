package com.example.model.mq.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.example.model.base.MessageWrapper;
import com.example.model.dto.req.CouponRemindDTO;
import com.example.model.dto.req.CouponRemindDelayEvent;
import com.example.model.executor.CouponRemindAsyncExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/11/21 20:58
 * @description
 */
@RocketMQMessageListener(
        topic = "zrq-spc-task-excel-topic-batch-distribution-rebuild-A",
        consumerGroup = "zrq-spc-task-remind-user-consumer-group-rebuild-A",
        selectorExpression = "zrq-spc-task-remind-user-rebuild-A"
)
@RequiredArgsConstructor
@Component
@Slf4j(topic = "RocketMqCouponRemindConsumer")
public class RocketMqCouponRemindConsumer implements RocketMQListener<MessageWrapper<CouponRemindDelayEvent>> {

    private final CouponRemindAsyncExecutor couponRemindAsyncExecutor;

    @Override
    public void onMessage(MessageWrapper<CouponRemindDelayEvent> messageWrapper) {
        CouponRemindDelayEvent remindDelayEvent = messageWrapper.getMsg();
        log.info("优惠券预定提醒consumer--->{}", remindDelayEvent);
        CouponRemindDTO remindDTO = BeanUtil.toBean(remindDelayEvent, CouponRemindDTO.class);
        remindDTO.setContact("1919616532");
        couponRemindAsyncExecutor.executeCouponRemind(remindDTO);
    }
}
