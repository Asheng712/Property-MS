package com.wisdom.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseApplicationPageQueryDTO extends PageQueryDTO {
    private Integer status;
    private String type;
    private String applicantName;
    private Long houseId;
}
