package com.example.model.mq.producer;

import cn.hutool.core.bean.BeanUtil;
import com.example.model.base.MessageWrapper;
import com.example.model.base.RocketMqExcelMessageCheckDTO;
import com.example.model.dto.req.ExcelReqDTO;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author zrq
 * @time 2025/9/5 10:52
 * @description
 */
@Component
public class RocketMqExcelProducer extends AbstractRocketMqMessageProducerTemplate<ExcelReqDTO> {

    public RocketMqExcelProducer(RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected RocketMqExcelMessageCheckDTO buildRocketMqExcelMessageCheckParam(ExcelReqDTO requestParam) {
        return BeanUtil.copyProperties(requestParam, RocketMqExcelMessageCheckDTO.class);
    }

    @Override
    protected Message<?> buildMessage(RocketMqExcelMessageCheckDTO rocketMqExcelMessageCheckDTO, ExcelReqDTO requestParam) {
        MessageWrapper<RocketMqExcelMessageCheckDTO> wrapper = new MessageWrapper<>();
        wrapper.setMsgId(UUID.randomUUID().toString());
        wrapper.setTag("mock-excel-producer-tag");
        wrapper.setKey(requestParam.getTaskId());
        wrapper.setMsg(rocketMqExcelMessageCheckDTO);
        return MessageBuilder.withPayload(wrapper)
                .setHeader(MessageConst.PROPERTY_KEYS, wrapper.getKey())
                .setHeader(MessageConst.PROPERTY_TAGS, wrapper.getTag())
                .build();
    }
}
