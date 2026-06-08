package com.wisdom.enumeration;

import lombok.Getter;

@Getter
public enum FeeTypeEnum {
    RENT(1, "租金"),
    PURCHASE(2, "买房金额"),
    DEPOSIT(3, "押金"),
    PROPERTY(4, "物业费");

    private final Integer code;
    private final String label;

    FeeTypeEnum(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public static FeeTypeEnum fromCode(Integer code) {
        if (code == null) return null;
        for (FeeTypeEnum e : values()) {
            if (e.code.equals(code)) return e;
        }
        return null;
    }
}
