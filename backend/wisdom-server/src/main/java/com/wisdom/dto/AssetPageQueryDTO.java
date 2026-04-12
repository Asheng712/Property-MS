package com.wisdom.dto;

import lombok.Data;

@Data
public class AssetPageQueryDTO extends PageQueryDTO {
    private String name;
    private String type;
    private String status;
}