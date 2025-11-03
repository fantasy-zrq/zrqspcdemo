package com.ds.redisdemo.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author zrq
 * @time 2025/10/30 21:37
 * @description
 */
@Slf4j(topic = "RedisStreamConsumer")
@Configuration
@RequiredArgsConstructor
public class RedisStreamConsumer implements InitializingBean {

    private final RedissonClient redissonClient;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void afterPropertiesSet() {
        RStream<String, String> stream = redissonClient.getStream("zrq-test-redis-mq");
        if (!stream.isExists()) {
            stream.add(StreamAddArgs.entries(Map.of("init", "init")));
        }
        try {
            stream.createGroup(StreamCreateGroupArgs.name("zrq-test-redis-mq-consumer-group-1"));
        } catch (Exception ignore) {
            log.error("ignore error ---->{}", ignore.getMessage());
        }

        executor.execute(() -> {
            while (true) {
                Map<StreamMessageId, Map<String, String>> messageIdMapMap = stream.readGroup("zrq-test-redis-mq-consumer-group-1", "consumer-1",
                        StreamReadGroupArgs.greaterThan(StreamMessageId.NEVER_DELIVERED)
                                .count(1)
                                .timeout(Duration.ofMillis(2000L))
                );
                if (messageIdMapMap != null && !messageIdMapMap.isEmpty()) {
                    messageIdMapMap.forEach((messageId, messageMap) -> {
                        log.info("message-id-->{}", messageId);
                        messageMap.forEach((key, value) -> {
                            log.info("message-key-->{}", key);
                            log.info("message-value-->{}", value);
                        });
                        stream.ack("zrq-test-redis-mq-consumer-group-1", messageId);
                    });
                }
            }
        });
    }
}
