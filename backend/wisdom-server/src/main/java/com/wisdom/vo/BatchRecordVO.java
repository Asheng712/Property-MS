package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BatchRecordVO {
    private Long id;
    private String batchNo;
    private String feeType;
    private String targetRange;
    private Integer totalCount;
    private BigDecimal totalAmount;
    private String status;
    private String operator;
    private LocalDateTime createTime;
}