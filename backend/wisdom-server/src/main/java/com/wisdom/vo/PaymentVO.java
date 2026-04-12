package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentVO {
    private Long id;
    private String trxNo;
    private Long billId;
    private String billNo;
    private Long houseId;
    private String houseName;
    private BigDecimal payAmount;
    private String payType;
    private Integer status;
    private String statusText;
    private LocalDateTime payTime;
    private String operator;
}