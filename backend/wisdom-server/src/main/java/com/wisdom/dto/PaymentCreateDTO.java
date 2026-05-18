package com.wisdom.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentCreateDTO {
    private Long billId;
    private String payType;
    private BigDecimal payAmount;
}
