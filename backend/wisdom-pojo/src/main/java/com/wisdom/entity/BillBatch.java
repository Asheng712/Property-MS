package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bus_bill_batch")
public class BillBatch implements Serializable {
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