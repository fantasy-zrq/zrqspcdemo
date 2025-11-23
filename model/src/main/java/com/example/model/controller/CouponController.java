package com.example.model.controller;

import com.example.model.common.result.Result;
import com.example.model.common.result.Results;
import com.example.model.dto.req.*;
import com.example.model.dto.resp.CouponRemindQueryRespDTO;
import com.example.model.dto.resp.CouponTemplateQueryRespDTO;
import com.example.model.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/api/spc/model/coupon/query")
    public Result<CouponTemplateQueryRespDTO> findCouponTemplate(CouponTemplateQueryReqDTO requestParam) {
        return Results.success(couponService.findCouponTemplate(requestParam));
    }

    @PostMapping("/api/spc/model/coupon/redeem")
    public Result<Void> redeemUserCoupon(@RequestBody CouponTemplateRedeemReqDTO requestParam) {
        couponService.redeemUserCoupon(requestParam);
        return Results.success();
    }

    @PostMapping("/api/spc/model/coupon/remind")
    public Result<Void> remindUserCoupon(@RequestBody CouponTemplateRemindCreateReqDTO requestParam) {
        couponService.createCouponRemind(requestParam);
        return Results.success();
    }

    @PostMapping("/api/spc/model/coupon/cancel-remind")
    public Result<Void> remindCancelUserCoupon(@RequestBody CouponTemplateRemindCreateReqDTO requestParam) {
        couponService.cancelCouponRemind(requestParam);
        return Results.success();
    }

    @PostMapping("/api/spc/model/coupon/query-remind")
    public Result<List<CouponRemindQueryRespDTO>> queryCancelUserCoupon(@RequestBody CouponTemplateQueryRemindReqDTO requestParam) {
        return Results.success(couponService.queryCouponRemind(requestParam));
    }
}
