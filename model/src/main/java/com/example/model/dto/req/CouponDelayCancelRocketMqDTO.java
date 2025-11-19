package com.example.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/11/19 20:22
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDelayCancelRocketMqDTO {

    private Long couponId;

    private Long userId;

    private Long delayTime;
}
