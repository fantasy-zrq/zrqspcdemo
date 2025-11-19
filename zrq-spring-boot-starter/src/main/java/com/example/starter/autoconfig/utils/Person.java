package com.example.starter.autoconfig.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/11/17 17:01
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    public String name;
    public Integer age;

    public void say(String nums){
        System.out.println(nums);
    }
}
