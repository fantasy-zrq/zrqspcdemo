package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.common.configs.MybatisPlusBaseMetaDO;
import lombok.*;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * @author zrq
 * @time 2025/9/7 20:33
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_coupon")
public class CouponDO extends MybatisPlusBaseMetaDO {
    /**
     * 优惠券id
     */
    @TableId(type = ASSIGN_ID)
    private Long couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券数量
     */
    private Integer couponStock;

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
     * 优惠商品编码
     */
    private String goods;

    /**
     * 优惠类型 0：立减券 1：满减券 2：折扣券
     */
    private Integer type;

    /**
     * 消耗规则 json存储
     */
    private String consumeRule;

    /**
     * 优惠券状态-0:启用-1:废弃
     */
    private Integer couponStatus;

    /**
     * 0-未删除，1已删除
     */
    private Integer delFlag;

    /**
     * 优惠券开始时间
     */
    private Date startTime;

    /**
     * 优惠券到期时间
     */
    private Date endTime;
}
