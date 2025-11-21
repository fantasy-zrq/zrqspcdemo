package com.example.model.mq.producer;

import cn.hutool.core.lang.UUID;
import com.example.model.base.MessageWrapper;
import com.example.model.base.RocketMqExcelMessageCheckDTO;
import com.example.model.dto.req.CouponRemindDelayEvent;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/11/21 20:14
 * @description
 */
@Component
public class RocketMqCouponRemindProducer extends AbstractRocketMqMessageProducerTemplate<CouponRemindDelayEvent> {

    public RocketMqCouponRemindProducer(RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected RocketMqExcelMessageCheckDTO buildRocketMqExcelMessageCheckParam(CouponRemindDelayEvent requestParam) {
        return RocketMqExcelMessageCheckDTO.builder()
                .topic("zrq-spc-task-excel-topic-batch-distribution-rebuild-A")
                .tags("zrq-spc-task-remind-user-rebuild-A")
                .msgTip("用户提醒消息发送成功")
                .keys(UUID.randomUUID().toString())
                .delayTime(requestParam.getDelayTime())
                .build();
    }

    @Override
    protected Message<?> buildMessage(RocketMqExcelMessageCheckDTO rocketMqExcelMessageCheckDTO, CouponRemindDelayEvent requestParam) {

        return MessageBuilder
                .withPayload(MessageWrapper.builder()
                        .msgId(requestParam.getCouponId().toString())
                        .tag(rocketMqExcelMessageCheckDTO.getTags())
                        .key(rocketMqExcelMessageCheckDTO.getKeys())
                        .msg(requestParam)
                        .build())
                .setHeader(MessageConst.PROPERTY_TAGS, rocketMqExcelMessageCheckDTO.getTags())
                .setHeader(MessageConst.PROPERTY_KEYS, rocketMqExcelMessageCheckDTO.getKeys())
                .build();
    }
}
