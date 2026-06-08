package com.wisdom.enumeration;

import lombok.Getter;

@Getter
public enum BillStatusEnum {
    PENDING(0, "待缴费"),
    SUBMITTED(1, "待核销"),
    PAID(2, "已缴费"),
    CANCELLED(3, "已撤销"),
    VOIDED(4, "已作废");

    private final Integer code;
    private final String label;

    BillStatusEnum(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public static BillStatusEnum fromCode(Integer code) {
        if (code == null) return null;
        for (BillStatusEnum e : values()) {
            if (e.code.equals(code)) return e;
        }
        return null;
    }
}
