package com.example.model.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/9/10 14:50
 * @description
 */
@Data
public class CouponCreateReqDTO {

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券数量
     */
    private Integer couponStock;

    /**
     * 优惠券状态-0:启用-1:废弃
     */
    private Integer couponStatus;

    /**
     * 该张优惠券能被一个用户领取多少次
     */
    private Integer receiveNumber;

    /**
     * 优惠券开始时间
     */

    private Date startTime;

    /**
     * 优惠券到期时间
     */
    private Date endTime;
}
