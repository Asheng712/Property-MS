package com.wisdom.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileTaskVO {
    private Long id;
    private String taskType;
    private String fileName;
    private String operator;
    private Integer dataCount;
    private String status;
    private String fileUrl;
    private LocalDateTime createTime;
}