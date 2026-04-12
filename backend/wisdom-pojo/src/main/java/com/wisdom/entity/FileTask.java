package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_file_task")
public class FileTask implements Serializable {
    private Long id;
    private String taskType;
    private String fileName;
    private String operator;
    private Integer dataCount;
    private String status;
    private String fileUrl;
    private LocalDateTime createTime;
}