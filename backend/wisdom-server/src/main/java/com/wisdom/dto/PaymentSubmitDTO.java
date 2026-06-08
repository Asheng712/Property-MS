package com.wisdom.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentSubmitDTO {
    @NotEmpty(message = "请选择账单")
    private List<Long> billIds;

    @NotNull(message = "请选择支付方式")
    private Integer payMethod;

    private String voucherUrl;

    private String remark;
}
