package com.wisdom.dto;

import lombok.Data;

@Data
public class PaymentAuditDTO {
    private Long id;
    private Integer status;
    private String operator;
}