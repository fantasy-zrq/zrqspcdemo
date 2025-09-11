package com.example.model.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/9/7 20:31
 * @description
 */
@Data
public class TaskCreateReqDTO{

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     * 任务状态 0：待执行 1：执行中 2：执行失败 3：执行成功 4：取消
     */
    private Integer taskStatus;
}
