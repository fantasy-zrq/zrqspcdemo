package com.example.model.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum CouponRemindTypeEnum {

    popUpWindow(0),
    mail(1);

    private final Integer type;

    public static CouponRemindTypeEnum getByType(Integer type) {
        for (CouponRemindTypeEnum remindEnum : values()) {
            if (Objects.equals(remindEnum.getType(), type)) {
                return remindEnum;
            }
        }
        return null;
    }
}
