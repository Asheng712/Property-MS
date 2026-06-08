package com.wisdom.enumeration;

import lombok.Getter;

@Getter
public enum PaymentStatusEnum {
    PENDING(0, "待核销"),
    VERIFIED(1, "已核销"),
    REJECTED(2, "已驳回"),
    CANCELLED(3, "已撤销");

    private final Integer code;
    private final String label;

    PaymentStatusEnum(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public static PaymentStatusEnum fromCode(Integer code) {
        if (code == null) return null;
        for (PaymentStatusEnum e : values()) {
            if (e.code.equals(code)) return e;
        }
        return null;
    }
}
