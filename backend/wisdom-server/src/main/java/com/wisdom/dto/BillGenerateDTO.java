package com.wisdom.dto;

import lombok.Data;

@Data
public class BillGenerateDTO {
    private Integer feeType;
    private String billMonth;
    private Long houseId;
    private String remark;
}
