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
     * 优惠券分发任务id
     */
    private Long taskId;

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
     * 优惠券开始时间
     */
    private Date startTime;

    /**
     * 优惠券到期时间
     */
    private Date endTime;

    /**
     * 用于判断是否是最后一批次
     */
    private Boolean lastBatch;

    /**
     * 有效期结束时间
     */
    private Date validEndTime;

    /**
     * 批量保存用户优惠券 Set 长度，默认满 5000 才会批量保存数据库
     */
    private Integer batchUserSetSize;
}
