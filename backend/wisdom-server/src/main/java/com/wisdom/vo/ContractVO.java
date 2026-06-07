package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ContractVO {
    private Long id;
    private Long houseId;
    private String houseName;
    private String tenantName;
    private BigDecimal rentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal increaseRate;
    private BigDecimal deposit;
    private Integer contractStatus;
    private String contractStatusText;
    private LocalDateTime createTime;
}