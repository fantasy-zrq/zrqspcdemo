package com.example.model.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zrq
 * @time 2025/8/5 14:35
 * @description
 */
@RestController
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/api/spc/model/user/login")
    public void login() {
        log.info("登录..");
    }

    @RequestMapping("/api/spc/model/user/register")
    public void register() {
        log.info("注册..");
    }
}
