package com.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zrq
 * @time 2025/8/30 19:26
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDO {
    private Long orderId;
    private String orderNo;
    private String purchaseName;
    private String productName;
    private BigDecimal price;
    private Integer orderTarget;
}
