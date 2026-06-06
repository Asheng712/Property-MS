package com.wisdom.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseApprovalDTO {
    private Long id;
    private Boolean approved;
    private BigDecimal proposedPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal deposit;
    private BigDecimal increaseRate;
    private String remark;
}
