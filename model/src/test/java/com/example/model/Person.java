package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zrq
 * @time 2025/8/30 16:19
 * @description
 */
@Component
@AllArgsConstructor
@Data
public class Person {
    private final String name = "zrq";
    private final Integer age = 18;
}
