package com.example;

import com.example.beanfactory.SummerApplicationContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/23 15:33
 * @description
 */
@Slf4j
public class SummerApplication {
    public static void main(String[] args) {
        SummerApplicationContext context = SummerApplicationContext.run(SummerApplication.class, args);
        context.getBeanDefinitionMap().values().forEach(System.out::println);
    }
}
