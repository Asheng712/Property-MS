package com.wisdom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentVerifyDTO {
    @NotNull(message = "支付记录ID不能为空")
    private Long id;
}
