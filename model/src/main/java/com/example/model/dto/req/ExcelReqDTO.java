package com.example.model.dto.req;

import lombok.Data;

/**
 * @author zrq
 * @time 2025/9/4 14:43
 * @description
 */
@Data
public class ExcelReqDTO {
    private String fileAddr;

    private String taskId;

    private String taskName;
}
