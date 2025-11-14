package com.example.model.mq.producer;

import com.example.model.base.RocketMqExcelMessageCheckDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author zrq
 * @time 2025/9/5 10:21
 * @description
 */
@Data
@RequiredArgsConstructor
@Component
@Slf4j(topic = "AbstractRocketMqMessageProducerTemplate")
public abstract class AbstractRocketMqMessageProducerTemplate<T> {

    private final RocketMQTemplate rocketMQTemplate;
//    private final String topic1 = "zrq-spc-mock-excel-topic";
    private final String TOPIC = "zrq-spc-task-excel-topic-batch-distribution";

    protected abstract RocketMqExcelMessageCheckDTO buildRocketMqExcelMessageCheckParam(T requestParam);

    protected abstract Message<?> buildMessage(RocketMqExcelMessageCheckDTO rocketMqExcelMessageCheckDTO, T requestParam);

    public SendResult senMessage(T requestParam) {
        RocketMqExcelMessageCheckDTO checkDTO = buildRocketMqExcelMessageCheckParam(requestParam);
        //0立即发送
        SendResult sendResult;
        if (Objects.equals(checkDTO.getDelayTime(), 0)) {
            sendResult = rocketMQTemplate.syncSend(TOPIC, buildMessage(checkDTO, requestParam));
        } else {
            sendResult = rocketMQTemplate.syncSendDelayTimeSeconds(TOPIC, buildMessage(checkDTO, requestParam), Long.parseLong(String.valueOf(checkDTO.getDelayTime())));
        }
        return sendResult;
    }
}
