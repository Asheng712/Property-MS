package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("bus_repair")
public class Repair implements Serializable {
    private Long id;
    private String repairNo;
    private Long houseId;
    private String content;
    private String reporter;
    private Long workerId;
    private Integer status;
    private Integer priority;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
}