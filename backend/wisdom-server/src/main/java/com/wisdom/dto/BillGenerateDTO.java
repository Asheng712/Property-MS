package com.wisdom.dto;

import lombok.Data;

@Data
public class BillGenerateDTO {
    private String feeType;
    private String targetRange;
    private String month;
}