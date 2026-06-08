package com.wisdom.dto;

import lombok.Data;

@Data
public class PaymentPageQueryDTO extends PageQueryDTO {
    private String paymentNo;
    private Long houseId;
    private Integer status;
    private Integer payMethod;
}
