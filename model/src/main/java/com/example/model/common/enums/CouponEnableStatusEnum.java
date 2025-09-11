package com.example.model.common.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author zrq
 */
@RequiredArgsConstructor
public enum CouponEnableStatusEnum {

    ACTIVE(0),
    END(1);
    public final Integer status;
}
