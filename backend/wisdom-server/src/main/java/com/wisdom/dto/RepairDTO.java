package com.wisdom.dto;

import lombok.Data;

@Data
public class RepairDTO {
    private Long id;
    private Long houseId;
    private String content;
    private String reporter;
    private Long reporterId;
    private Integer priority;
}