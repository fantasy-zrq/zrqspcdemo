package com.example.model.dto.req;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zrq
 * @time 2025/11/23 16:11
 * @description
 */
@Data
public class CouponCreatePaymentReqDTO {
    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 折扣后金额
     */
    private BigDecimal payableAmount;

    /**
     * 店铺编号
     */
    private String shopNumber;

    /**
     * 商品集合
     */
    private List<CouponCreatePaymentGoodsReqDTO> goodsList;
}
