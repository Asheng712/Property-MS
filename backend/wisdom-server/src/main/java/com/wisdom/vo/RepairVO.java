package com.wisdom.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RepairVO {
    private Long id;
    private String repairNo;
    private Long houseId;
    private String houseName;
    private String content;
    private String reporter;
    private Long workerId;
    private String workerName;
    private Integer status;
    private String statusText;
    private Integer priority;
    private String priorityText;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
}