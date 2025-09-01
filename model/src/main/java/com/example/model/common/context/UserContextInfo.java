package com.example.model.common.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zrq
 * @time 2025/8/7 15:07
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContextInfo {
    private Long id;
    private String username;
    private Integer age;
    private Integer sex;
}
