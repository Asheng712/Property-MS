package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRecordDetailVO {
    private Long id;
    private Long billId;
    private String billNo;
    private String houseName;
    private Integer feeType;
    private String feeTypeText;
    private String billMonth;
    private BigDecimal amount;
}
