package com.example.starter.autoconfig.log;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/28 16:21
 * @description
 */
@Slf4j
public class MyLogImpl {
    public void say(String name, Integer age) {
        log.info("my name is {},age {}", name, age);
    }
}
