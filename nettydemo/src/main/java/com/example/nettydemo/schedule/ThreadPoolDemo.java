package com.example.nettydemo.schedule;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author zrq
 * @time 2025/8/13 13:36
 * @description
 */
@Configuration
@ConfigurationProperties(prefix = "thread-pool.config")
@Data
public class ThreadPoolDemo {

    private Integer coreThreadSize;
    private Integer maxThreadSize;
    private Integer blockQueueSize;
    private String threadNamePrefix;
    private Integer keepAliveSeconds;

    @Bean
    public ThreadPoolExecutor myExecutor() {
        return new ThreadPoolExecutor(coreThreadSize, maxThreadSize, keepAliveSeconds,
                TimeUnit.SECONDS, new LinkedBlockingDeque<>(blockQueueSize), runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(threadNamePrefix);
            thread.setDaemon(false);
            return thread;
        });
    }
}
