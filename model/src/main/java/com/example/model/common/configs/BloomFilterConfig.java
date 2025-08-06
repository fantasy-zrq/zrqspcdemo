package com.example.model.common.configs;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zrq
 * @time 2025/8/6 21:21
 * @description
 */
@Configuration
public class BloomFilterConfig {

    @Bean
    public RBloomFilter<String> userNameBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("userNameBloomFilterConfig");
        bloomFilter.tryInit(100000000L, 0.001);
        return bloomFilter;
    }
}
