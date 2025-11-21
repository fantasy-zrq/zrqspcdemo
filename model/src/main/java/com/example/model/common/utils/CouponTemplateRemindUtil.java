package com.example.model.common.utils;

import com.example.model.common.exception.ClientException;

/**
 * @author zrq
 * @time 2025/11/21 19:34
 * @description 用于将remindTime和type融合为一个字段，使用范围和0/1来唯一限定
 */
public class CouponTemplateRemindUtil {
    private final static Integer TIME_RANGE = 12;
    private final static Integer STEP = 5;

    public static Long combineTypeAndRemindTime(Integer remindTime, Integer type) {
        if (remindTime > TIME_RANGE * STEP) {
            throw new ClientException("超时时间不允许超过--[" + TIME_RANGE * STEP + "m]");
        }
        return 1L << (type * TIME_RANGE + Integer.max(0, (remindTime / STEP) - 1));
    }
}
