package com.wisdom.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PurchaseApplicationVO {
    private Long id;
    private String applicationNo;
    private String type;
    private Long houseId;
    private String houseName;
    private Long applicantId;
    private String applicantName;
    private String applicantPhone;
    private Integer status;
    private String statusText;
    private BigDecimal proposedPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String remark;
    private Long createdContractId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
