package com.wisdom.dto;

import lombok.Data;

@Data
public class AssetPageQueryDTO {

    private Integer page;
    private Integer pageSize;
    private String name;
    private String type;
    private String status;

}