package com.wisdom.enumeration;

import lombok.Getter;

@Getter
public enum PayMethodEnum {
    WECHAT(1, "微信支付"),
    ALIPAY(2, "支付宝"),
    BANK(3, "银行卡"),
    CASH(4, "现金");

    private final Integer code;
    private final String label;

    PayMethodEnum(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public static PayMethodEnum fromCode(Integer code) {
        if (code == null) return null;
        for (PayMethodEnum e : values()) {
            if (e.code.equals(code)) return e;
        }
        return null;
    }
}
