package com.example.starter.autoconfig.config;

import com.example.starter.autoconfig.log.NoDuplicateCommitAspect;
import com.example.starter.autoconfig.log.NoMQDuplicateConsumeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zrq
 * @time 2025/9/1 14:20
 * @description
 */
public class NoDuplicateCommitAutoConfig {
    @Bean
    public NoDuplicateCommitAspect noDuplicateCommitAspect(StringRedisTemplate stringRedisTemplate) {
        return new NoDuplicateCommitAspect(stringRedisTemplate);
    }

    @Bean
    public NoMQDuplicateConsumeAspect noMQDuplicateConsumeAspect(StringRedisTemplate stringRedisTemplate) {
        return new NoMQDuplicateConsumeAspect(stringRedisTemplate);
    }
}
