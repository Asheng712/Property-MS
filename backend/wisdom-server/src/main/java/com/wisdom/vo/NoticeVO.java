package com.wisdom.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoticeVO {
    private Long id;
    private String title;
    private String content;
    private String targetType;
    private String status;
    private Integer viewCount;
    private LocalDateTime createTime;
}