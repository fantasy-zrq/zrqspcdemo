package com.example.gateway.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/8/5 15:14
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    private String message;

    private Integer code;

    private Boolean success;
}
