package com.wisdom.dto;

import lombok.Data;

@Data
public class BillPageQueryDTO extends PageQueryDTO {
    private String billNo;
    private Long houseId;
    private Integer feeType;
    private Integer status;
    private String billMonth;
}
