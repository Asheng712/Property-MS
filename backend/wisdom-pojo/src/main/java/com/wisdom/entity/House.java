package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bus_house")
public class House implements Serializable {
    private Long id;
    private Long parentId;
    private String name;
    private String type;
    private BigDecimal area;
    private String status;
    private String ownerName;
    private String ownerPhone;
    private LocalDateTime createTime;
}