package com.example.model.dto.resp;

import lombok.Data;

/**
 * @author zrq
 * @time 2025/11/23 15:07
 * @description
 */
@Data
public class CouponRemindQueryRespDTO {

    /**
     * 预约人的用户ID
     */
    private Long userId;

    /**
     * 优惠券模板id
     */
    private Long couponId;

    /**
     * 提醒方式-手机 APP 弹框提醒、邮件提醒等，我们这里0默认手机 APP 弹框提醒。
     */
    private Integer type;

    /**
     * 提醒时间，比如五分钟，十分钟，十五分钟
     */
    private Integer remindTime;
}
