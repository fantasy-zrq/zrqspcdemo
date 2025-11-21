package com.example.model.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class CouponRemindDTO {

    /**
     * 优惠券模板id
     */
    private String couponId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户联系方式，可能是邮箱、手机号、等等
     */
    private String contact;

    /**
     * 提醒方式
     */
    private Integer type;

    /**
     * 提醒时间，比如五分钟，十分钟，十五分钟
     */
    private Integer remindTime;

    /**
     * 开抢时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
}
