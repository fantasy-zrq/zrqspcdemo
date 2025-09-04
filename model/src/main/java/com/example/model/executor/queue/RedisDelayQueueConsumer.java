package com.example.model.executor.queue;

import com.example.model.dto.req.ExcelReqDTO;
import com.example.model.executor.ExcelResolverThreadPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zrq
 * @time 2025/9/4 15:32
 * @description
 */
@Slf4j(topic = "RedisDelayQueueConsumer")
@Component
@RequiredArgsConstructor
public class RedisDelayQueueConsumer implements CommandLineRunner {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final ExcelResolverThreadPool excelResolverThreadPool;
    private final ExecutorService executor = new ThreadPoolExecutor(2,
            5,
            500L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5),
            runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName("thread-pool-mock-consumer-excel");
                return thread;
            }
    );

    @Override
    public void run(String... args) {
        RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue("mock-excel-block-queue");
        executor.execute(() -> {
            while (true) {
                ExcelReqDTO requestParam;
                try {
                    requestParam = (ExcelReqDTO) blockingQueue.take();
                    if (Objects.equals(stringRedisTemplate.hasKey("zrq:spc:mock:excel:rowcount"), Boolean.FALSE)) {
                        //代表插入任务失败了得重试
                        log.info("RedisDelayQueueConsumer 进行excel行数插入重试~~~");
                        excelResolverThreadPool.execute(requestParam);
                    }
                } catch (InterruptedException ignore) {}
            }
        });
    }
}
