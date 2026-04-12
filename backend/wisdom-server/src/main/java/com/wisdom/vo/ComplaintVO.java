package com.wisdom.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComplaintVO {
    private Long id;
    private String complaintNo;
    private String category;
    private String content;
    private String source;
    private Integer status;
    private String statusText;
    private String handleResult;
    private LocalDateTime createTime;
}