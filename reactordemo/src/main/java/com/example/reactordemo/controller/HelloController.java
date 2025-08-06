package com.example.reactordemo.controller;

import com.example.reactordemo.entity.Person;
import com.example.reactordemo.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author zrq
 * @time 2025/8/6 13:01
 * @description
 */
@RestController
public class HelloController {

    @RequestMapping("/api/t1")
    public Mono<Result<Object>> t1() {
        Person person = new Person("张三", 18, "男");
        Result<Object> result = Result.builder()
                .code(HttpStatus.OK.value())
                .message("请求成功")
                .success(Boolean.TRUE)
                .data(person)
                .build();
        return Mono.just(result);
    }
}
