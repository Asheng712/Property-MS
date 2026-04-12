package com.wisdom.dto;

import lombok.Data;

@Data
public class ContractPageQueryDTO extends PageQueryDTO {
    private String tenantName;
    private Integer contractStatus;
}