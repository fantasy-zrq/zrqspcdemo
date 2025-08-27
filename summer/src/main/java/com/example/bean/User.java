package com.example.bean;

import com.example.annotation.Autowired;
import com.example.annotation.Component;
import com.example.annotation.MyLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/8/23 16:00
 * @description
 */
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String name = "zrq";
    private Integer age = 18;
    @Autowired
//    @ToString.Exclude
    private Dog dog;

    @MyLog
    public void say(){
        log.info("aop say method..");
    }
}
