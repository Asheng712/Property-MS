package com.wisdom.dto;

import lombok.Data;

@Data
public class PurchaseApplicationDTO {
    private Long houseId;
    private String type;
    private String applicantName;
    private String applicantPhone;
    private String remark;
}
