package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("bus_complaint")
public class Complaint implements Serializable {
    private Long id;
    private String complaintNo;
    private String category;
    private String content;
    private String source;
    private Integer status;
    private LocalDateTime createTime;
}