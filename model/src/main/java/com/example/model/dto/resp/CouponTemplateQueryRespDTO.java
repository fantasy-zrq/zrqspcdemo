package com.example.model.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class CouponTemplateQueryRespDTO {

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 该张优惠券能被一个用户领取多少次
     */
    private Integer receiveNumber;

    /**
     * 店铺编号
     */
    private Long shopNumber;

    /**
     * 优惠券来源 0：店铺券 1：平台券
     */
    private Integer source;

    /**
     * 优惠对象 0：商品专属 1：全店通用
     */
    private Integer target;

    /**
     * 适用于的商品Id
     */
    private String goods;

    /**
     * 消耗规则 json存储
     */
    private String consumeRule;

    /**
     * 有效期开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 有效期结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 库存
     */
    private Integer couponStock;

    /**
     * 优惠券状态 0：生效中 1：已结束
     */
    private Integer couponStatus;
}
