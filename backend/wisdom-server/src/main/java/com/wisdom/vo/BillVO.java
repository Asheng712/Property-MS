package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillVO {
    private Long id;
    private String billNo;
    private Long houseId;
    private String houseName;
    private BigDecimal amount;
    private String type;
    private Integer payStatus;
    private String payStatusText;
    private LocalDate deadline;
    private LocalDateTime createTime;
}