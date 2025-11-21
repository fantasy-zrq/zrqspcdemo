package com.example.model.mq.producer;

import cn.hutool.core.lang.UUID;
import com.example.model.base.MessageWrapper;
import com.example.model.base.RocketMqExcelMessageCheckDTO;
import com.example.model.dto.req.CouponDelayCancelRocketMqDTO;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/11/19 20:18
 * @description
 */
@Component
public class RocketMqCouponDelayCancelProducer extends AbstractRocketMqMessageProducerTemplate<CouponDelayCancelRocketMqDTO> {

    public RocketMqCouponDelayCancelProducer(RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected RocketMqExcelMessageCheckDTO buildRocketMqExcelMessageCheckParam(CouponDelayCancelRocketMqDTO requestParam) {
        String TOPIC = "zrq-spc-task-excel-topic-batch-distribution-rebuild-A";
        return RocketMqExcelMessageCheckDTO.builder()
                .msgTip("延迟消息发送成功")
                .keys(UUID.randomUUID().toString())
                .topic(TOPIC)
                .tags("zrq-spc-task-coupon-expire-delay-cancel-tag")
                .delayTime(requestParam.getDelayTime())
                .build();
    }

    @Override
    protected Message<?> buildMessage(RocketMqExcelMessageCheckDTO rocketMqExcelMessageCheckDTO, CouponDelayCancelRocketMqDTO requestParam) {
        MessageWrapper<Object> msg = MessageWrapper.builder()
                .msgId(requestParam.getCouponId().toString())
                .key(rocketMqExcelMessageCheckDTO.getKeys())
                .msg(requestParam)
                .tag("zrq-spc-task-coupon-expire-delay-cancel-tag")
                .build();
        return MessageBuilder
                .withPayload(msg)
                .setHeader(MessageConst.PROPERTY_TAGS, msg.getTag())
                .setHeader(MessageConst.PROPERTY_KEYS, rocketMqExcelMessageCheckDTO.getKeys())
                .build();
    }
}
