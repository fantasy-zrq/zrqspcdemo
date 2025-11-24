package com.example.model.dto.req;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CouponTemplateQueryReqDTO {

    /**
     * 优惠券模板id
     */
    private Long couponId;

    /**
     * 优惠券所属商家，可能为null
     */
    private Long shopNumber;
}
