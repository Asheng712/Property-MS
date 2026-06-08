package com.wisdom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCancelDTO {
    @NotNull(message = "支付记录ID不能为空")
    private Long id;

    @NotBlank(message = "撤销原因不能为空")
    private String cancelReason;
}
