package com.ds.redisdemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.stream.StreamAddArgs;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zrq
 */
@Slf4j(topic = "RedisDemoApplication")
@SpringBootApplication
@RequiredArgsConstructor
public class RedisDemoApplication implements CommandLineRunner{

    private final RedissonClient redissonClient;

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        RStream<String, String> stream = redissonClient.getStream("zrq-test-redis-mq");
        AtomicInteger count = new AtomicInteger(0);

        while (true) {
            if (count.getAndIncrement() == 200) {
                break;
            } else {
                StreamAddArgs<String, String> entries = StreamAddArgs.entries(Map.of(
                        "name", "zrq",
                        "age", "18",
                        "sex", "male",
                        "count", String.valueOf(count.get())
                ));
                stream.add(entries);
                log.info("{}", entries);
                Thread.sleep(200L);
            }
        }
    }
}
