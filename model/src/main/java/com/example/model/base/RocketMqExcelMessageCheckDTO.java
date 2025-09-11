package com.example.model.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/9/5 10:13
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RocketMqExcelMessageCheckDTO {

    private String fileAddr;

    private String taskId;

    private String taskName;

    private Integer sendType;

    private Integer delayTime;
}
