package com.example.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/11/21 20:03
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponRemindDelayEvent {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 券ID
     */
    private Long couponId;

    /**
     * 提醒方式
     */
    private Integer type;

    /**
     * 优惠券开抢时间
     */
    private Date startTime;

    /**
     * mq提醒用户的绝对时间
     */
    private Long delayTime;

    /**
     * 在优惠券开始前remindTime分钟提醒用户
     */
    private Integer remindTime;
}
