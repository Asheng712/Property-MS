package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("bus_bill")
public class Bill implements Serializable {
    private Long id;
    private Long batchId;
    private Long houseId;
    private String billNo;
    private BigDecimal amount;
    private String type;
    private Integer payStatus;
    private LocalDate deadline;
    private LocalDateTime createTime;
}