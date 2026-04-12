package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("bus_contract")
public class Contract implements Serializable {
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