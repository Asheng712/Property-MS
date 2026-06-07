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
@TableName("bus_purchase_application")
public class PurchaseApplication implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applicationNo;
    private Long houseId;
    private Long applicantId;
    private String applicantName;
    private String applicantPhone;
    private String type;
    private Integer status;
    private BigDecimal proposedPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String remark;
    private Long createdContractId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
