package com.example.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zrq
 * @time 2025/9/3 10:42
 * @description
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "zrq-consumer-group", topic = "zrq-spc-rocketmq")
public class RocketMqLister implements RocketMQListener<Map<String, Object>> {

    @Override
    public void onMessage(Map<String, Object> message) {
        log.info("consumer -->  {}", message);
    }
}
