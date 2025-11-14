package com.example.model.mq.producer;

import com.example.model.base.MessageWrapper;
import com.example.model.base.RocketMqExcelMessageCheckDTO;
import com.example.model.entity.CouponBatchDistributionDO;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/11/14 16:15
 * @description
 */
@Component
public class RocketMqCouponBatchDistributionProducer extends AbstractRocketMqMessageProducerTemplate<CouponBatchDistributionDO> {

    public RocketMqCouponBatchDistributionProducer(RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected RocketMqExcelMessageCheckDTO buildRocketMqExcelMessageCheckParam(CouponBatchDistributionDO requestParam) {

        return RocketMqExcelMessageCheckDTO.builder()
                .msgTip("优惠券发送成功")
                .delayTime(2000)
                .build();
    }

    @Override
    protected Message<?> buildMessage(RocketMqExcelMessageCheckDTO rocketMqExcelMessageCheckDTO, CouponBatchDistributionDO requestParam) {
        return MessageBuilder
                .withPayload(new MessageWrapper(requestParam.getCouponId().toString()
                        , "zrq-spc-task-excel-topic-batch-distribution-tag", rocketMqExcelMessageCheckDTO.getKeys(), requestParam))
                .setHeader(MessageConst.PROPERTY_KEYS, rocketMqExcelMessageCheckDTO.getKeys())
                .setHeader(MessageConst.PROPERTY_TAGS, "zrq-spc-task-excel-topic-batch-distribution-tag")
                .build();
    }
}
