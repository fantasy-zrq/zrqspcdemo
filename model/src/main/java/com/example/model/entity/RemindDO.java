package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zrq
 * @time 2025/11/21 18:55
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName("t_remind")
public class RemindDO {

    private Long id;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 券ID
     */
    private Long couponId;

    /**
     * 存储信息
     */
    private Long information;

    /**
     * 优惠券开抢时间
     */
    private Date startTime;

}
