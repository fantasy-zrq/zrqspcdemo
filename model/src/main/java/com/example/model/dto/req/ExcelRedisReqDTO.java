package com.example.model.dto.req;

import lombok.Data;

/**
 * @author zrq
 * @time 2025/9/4 19:33
 * @description
 */
@Data
public class ExcelRedisReqDTO {
    private String fileAddr;

    private String taskId;

    private String taskName;

    private Integer rowCount;
}
