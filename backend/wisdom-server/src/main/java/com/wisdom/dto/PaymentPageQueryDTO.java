package com.wisdom.dto;

import lombok.Data;

@Data
public class PaymentPageQueryDTO extends PageQueryDTO {
    private String trxNo;
    private Long houseId;
    private Integer status;
    private String payType;
}