package com.wisdom.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractDTO {
    private Long id;
    private Long houseId;
    private String tenantName;
    private BigDecimal rentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal increaseRate;
    private BigDecimal deposit;
    private Integer contractStatus;
}