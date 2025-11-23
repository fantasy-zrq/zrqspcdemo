package com.example.model.dto.req;

import lombok.Data;

/**
 * @author zrq
 * @time 2025/11/21 17:08
 * @description
 */
@Data
public class CouponTemplateQueryRemindReqDTO {

    /**
     * 预约人的用户ID
     */
    private Long userId;
    /**
     * 优惠券模板id
     */
    private Long couponId;
}
