package com.example.model.dto.req;

import lombok.Data;

@Data
public class CouponTemplateRedeemReqDTO {

    /**
     * 优惠券模板id
     */
    private Long couponId;

    /**
     * 优惠券接收人id
     */
    private Long userId;
}
