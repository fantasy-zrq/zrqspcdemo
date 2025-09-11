package com.example.model.controller;

import com.example.model.common.result.Result;
import com.example.model.common.result.Results;
import com.example.model.dto.req.CouponCreateReqDTO;
import com.example.model.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zrq
 * @time 2025/9/10 14:46
 * @description
 */
@RequiredArgsConstructor
@RestController
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/api/spc/model/coupon/create")
    public Result<String> createCoupon(@RequestBody CouponCreateReqDTO requestParam) {
        couponService.create(requestParam);
        return Results.success("创建成功");
    }
}
