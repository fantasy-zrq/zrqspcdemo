package com.example.bean;

import com.example.annotation.Autowired;
import com.example.annotation.Component;
import com.example.annotation.MyLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/23 16:00
 * @description
 */
@Component
@Data
@Slf4j
public class Dog {
    private String name = "小白";

    @Autowired
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
//
//    @Autowired
//    private Food food;

    @MyLog
    public void say(){
        log.info("aop say method..");
    }
}
