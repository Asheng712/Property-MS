package com.wisdom.dto;

import lombok.Data;

@Data
public class NoticePageQueryDTO extends PageQueryDTO {
    private String title;
    private String status;
    private String targetType;
}