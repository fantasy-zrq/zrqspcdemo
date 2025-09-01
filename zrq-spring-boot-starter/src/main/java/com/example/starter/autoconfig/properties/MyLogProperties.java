package com.example.starter.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zrq
 * @time 2025/8/28 16:11
 * @description
 */
@ConfigurationProperties(prefix = "zrq.auto")
@Data
public class MyLogProperties {
    private String name;
    private Integer age;
}
