package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentRecordVO {
    private Long id;
    private String paymentNo;
    private Long userId;
    private String userName;
    private BigDecimal amount;
    private Integer payMethod;
    private String payMethodText;
    private LocalDateTime payTime;
    private Integer status;
    private String statusText;
    private String voucherUrl;
    private String remark;
    private Long verifyUser;
    private String verifyUserName;
    private LocalDateTime verifyTime;
    private Long cancelUser;
    private String cancelUserName;
    private LocalDateTime cancelTime;
    private String cancelReason;
    private List<PaymentRecordDetailVO> bills;
    private LocalDateTime createTime;
}
