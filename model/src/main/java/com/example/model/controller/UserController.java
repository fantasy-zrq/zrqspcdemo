package com.example.model.controller;

import com.example.model.common.annotation.LogConsumerTime;
import com.example.model.common.result.Result;
import com.example.model.common.result.Results;
import com.example.model.dto.req.UserLoginReqDTO;
import com.example.model.dto.req.UserRegisterReqDTO;
import com.example.model.entity.OrderDO;
import com.example.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zrq
 * @time 2025/8/5 14:35
 * @description
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/spc/model/user/login")
    @LogConsumerTime
    public Result<String> login(@RequestBody UserLoginReqDTO requestParam) {
        return Results.success(userService.doLogin(requestParam));
    }

    @PostMapping("/api/spc/model/user/register")
    @LogConsumerTime
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.doRegister(requestParam);
        return Results.success();
    }

    @RequestMapping("/api/spc/model/user/test")
    @LogConsumerTime
    public void test() {
        log.info("test..");
    }

    @PostMapping("/api/spc/model/user/order")
    public Result<String> mockOrder(@RequestBody OrderDO orderDO) {
        userService.mockOrder(orderDO);
        return Results.success("测试通过");
    }
}
