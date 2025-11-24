package com.example.model.dto.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zrq
 * @time 2025/11/23 16:47
 * @description
 */
@Data
public class CouponCreatePaymentGoodsReqDTO {
    /**
     * 商品编号
     */
    private String goodsNumber;

    /**
     * 商品价格
     */
    private BigDecimal goodsAmount;

    /**
     * 商品折扣后金额
     */
    private BigDecimal goodsPayableAmount;
}
