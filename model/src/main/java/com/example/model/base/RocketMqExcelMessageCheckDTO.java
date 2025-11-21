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

    private String msgTip;

    private String keys;

    private String tags;

    private Long delayTime;

    private String topic;
}
