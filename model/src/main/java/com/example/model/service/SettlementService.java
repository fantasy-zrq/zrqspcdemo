package com.example.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.req.CouponCreatePaymentReqDTO;
import com.example.model.dto.req.CouponProcessPaymentReqDTO;
import com.example.model.dto.req.CouponProcessRefundReqDTO;
import com.example.model.entity.SettlementDO;

public interface SettlementService extends IService<SettlementDO> {
    void createPaymentRecord(CouponCreatePaymentReqDTO requestParam);

    void processPayment(CouponProcessPaymentReqDTO requestParam);

    void processRefund(CouponProcessRefundReqDTO requestParam);
}
