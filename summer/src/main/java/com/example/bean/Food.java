package com.example.bean;

import com.example.annotation.Component;
import lombok.Data;

/**
 * @author zrq
 * @time 2025/8/24 10:48
 * @description
 */
@Data
@Component
public class Food {
    private String kinds = "cake";
    private String color = "black";
}
