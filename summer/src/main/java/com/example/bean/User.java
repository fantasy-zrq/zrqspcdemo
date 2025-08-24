package com.example.bean;

import com.example.annotation.Autowired;
import com.example.annotation.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/8/23 16:00
 * @description
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String name = "zrq";
    private Integer age = 18;
    @Autowired
    private Dog dog;
}
