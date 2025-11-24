package com.example.model.dto.req;

import lombok.Data;

/**
 * @author zrq
 * @time 2025/11/23 16:48
 * @description
 */
@Data
public class CouponProcessRefundReqDTO {
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
}
