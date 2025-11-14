package com.example.model.entity;

import lombok.*;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/11/14 16:00
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponBatchDistributionDO {
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券发放数量
     */
    private Integer sendNum;

    /**
     * 发送类型0：立即发送，1：延迟发送
     */
    private Integer sendType;

    /**
     * 延迟发送时间
     */
    private Date sendTime;

    /**
     * 任务状态 0：待执行 1：执行中 2：执行失败 3：执行成功 4：取消
     */
    private Integer taskStatus;

    /**
     * 任务完成时间
     */
    private Date taskCompleteTime;

    /**
     * 用于判断是否是最后一批次
     */
    private Boolean lastBatch;
}
