package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("bus_contract")
public class Contract implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long houseId;
    private String tenantName;
    private BigDecimal rentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal increaseRate;
    private BigDecimal deposit;
    private Integer contractStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}