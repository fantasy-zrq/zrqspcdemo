package com.example.bean;

import com.example.annotation.Autowired;
import com.example.annotation.Component;
import lombok.Data;

/**
 * @author zrq
 * @time 2025/8/23 16:00
 * @description
 */
@Component
@Data
public class Dog {
    private String name = "小白";

    @Autowired
    private Food food;
}
