package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.common.configs.MybatisPlusBaseMetaDO;
import lombok.*;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/9/7 20:31
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_task")
public class TaskDO extends MybatisPlusBaseMetaDO {
    /**
     * 任务id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * excel地址
     */
    private String fileAddress;

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
     * 任务发送人id
     */
    private Long operatorId;

    /**
     * del_flag
     */
    private Integer delFlag;
}
