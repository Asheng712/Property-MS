package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_notice")
public class Notice implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String targetType;
    private String status;
    private Integer viewCount;
    private LocalDateTime createTime;
}