package com.example.starter.autoconfig.config;

import com.example.starter.autoconfig.log.NoDuplicateCommitAspect;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * @author zrq
 * @time 2025/9/1 14:20
 * @description
 */
public class NoDuplicateCommitAutoConfig {
    @Bean
    public NoDuplicateCommitAspect noDuplicateCommitAspect(RedissonClient redissonClient) {
        return new NoDuplicateCommitAspect(redissonClient);
    }
}
