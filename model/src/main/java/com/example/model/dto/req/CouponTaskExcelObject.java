package com.example.model.dto.req;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zrq
 * @time 2025/9/10 20:12
 * @description
 */
@Data
public class CouponTaskExcelObject {

    @ExcelProperty("用户ID")
    private String userId;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("邮箱")
    private String mail;
}
