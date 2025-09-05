package com.example.model.mq.consumer;

import com.example.model.dto.req.ExcelReqDTO;
import com.example.model.executor.ExcelResolverThreadPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author zrq
 * @time 2025/9/4 19:56
 * @description
 */
@RocketMQMessageListener(
        consumerGroup = "zrq-spc-mock-excel-consumer-group",
        topic = "zrq-spc-mock-excel-topic"
)
@Component
@RequiredArgsConstructor
@Slf4j(topic = "RocketExcelConsumer")
public class RocketExcelConsumer implements RocketMQListener<ExcelReqDTO> {

    private final StringRedisTemplate stringRedisTemplate;
    private final ExcelResolverThreadPool excelResolverThreadPool;

    @Override
    public void onMessage(ExcelReqDTO message) {
        log.info("RocketExcelConsumer 开始消费消息-->[{}]", message);
        if (Objects.equals(stringRedisTemplate.hasKey("zrq:spc:mock:excel:rowcount"), Boolean.FALSE)) {
            //代表插入任务失败了得重试
            log.info("RedisDelayQueueConsumer 进行excel行数插入重试~~~");
            excelResolverThreadPool.execute(message);
            return;
        }
        log.info("redis中已经存入数据--->[{}]", message);
    }
}
