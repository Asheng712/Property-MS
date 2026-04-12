package com.wisdom.dto;

import lombok.Data;

@Data
public class BillPageQueryDTO extends PageQueryDTO {
    private String billNo;
    private Long houseId;
    private Integer payStatus;
    private String type;
}