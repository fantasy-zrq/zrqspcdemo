package com.example.model.controller;

import com.example.model.common.result.Result;
import com.example.model.common.result.Results;
import com.example.model.dto.req.CouponCreatePaymentReqDTO;
import com.example.model.dto.req.CouponProcessPaymentReqDTO;
import com.example.model.dto.req.CouponProcessRefundReqDTO;
import com.example.model.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zrq
 * @time 2025/11/23 15:41
 * @description
 */
@RequiredArgsConstructor
@RestController
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping("/api/spc/model/settlement/create-payment-record")
    public Result<Void> createPaymentRecord(@RequestBody CouponCreatePaymentReqDTO requestParam) {
        settlementService.createPaymentRecord(requestParam);
        return Results.success();
    }

    @PostMapping("/api/spc/model/settlement/process-payment")
    public Result<Void> processPayment(@RequestBody CouponProcessPaymentReqDTO requestParam) {
        settlementService.processPayment(requestParam);
        return Results.success();
    }

    @PostMapping("/api/spc/model/settlement/process-refund")
    public Result<Void> processRefund(@RequestBody CouponProcessRefundReqDTO requestParam) {
        settlementService.processRefund(requestParam);
        return Results.success();
    }
}
