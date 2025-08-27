package com.example.model.common.enums;

import com.example.model.common.exception.ClientException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zrq
 */
@RequiredArgsConstructor
@Getter
public enum MockOrderTargetEnum {

    MOCK_DISCOUNT_ORDER(0, "折扣单"),
    MOCK_ORIGINAL_PRICE_ORDER(1, "原价单");

    private final Integer type;
    private final String value;

    public static String findOrderValueByType(Integer type) {
        for (MockOrderTargetEnum targetEnum : MockOrderTargetEnum.values()) {
            if (targetEnum.type.equals(type)) {
                return targetEnum.value;
            }
        }
        throw new ClientException("没有该类型的枚举值");
    }
}
