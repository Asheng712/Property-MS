package com.wisdom.dto;

import lombok.Data;

@Data
public class ComplaintCreateDTO {
    private String category;
    private String content;
    private String source;
    private Long reporterId;
}
