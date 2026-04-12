package com.wisdom.dto;

import lombok.Data;

@Data
public class ComplaintPageQueryDTO extends PageQueryDTO {
    private String category;
    private Integer status;
}