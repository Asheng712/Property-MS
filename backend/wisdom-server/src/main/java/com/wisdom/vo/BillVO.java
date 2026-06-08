package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillVO {
    private Long id;
    private Long userId;
    private Long houseId;
    private String houseName;
    private Long contractId;
    private Integer feeType;
    private String feeTypeText;
    private String billNo;
    private String billMonth;
    private BigDecimal amount;
    private Integer status;
    private String statusText;
    private LocalDate dueDate;
    private String remark;
    private LocalDateTime createTime;
}
