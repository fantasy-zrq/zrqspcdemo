package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.common.configs.MybatisPlusBaseMetaDO;
import lombok.*;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/9/7 20:36
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_receive")
public class ReceiveDO extends MybatisPlusBaseMetaDO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 优惠券接收人id
     */
    private Long userId;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券接收数量
     */
    private Integer receiveNumber;

    /**
     * 优惠券接受时间
     */
    private Date receiveTime;

    /**
     * 优惠券开始时间
     */
    private Date startTime;

    /**
     * 优惠券结束时间
     */
    private Date endTime;

    /**
     * 状态 0：未使用 1：已使用 2：已过期
     */
    private Integer status;

    /**
     * 用户使用时间
     */
    private Date useTime;

    /**
     * 在excel中的真实行数
     */
    private Integer rowCount;

}
