package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bus_house")
public class House {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String name;

    private String type; // BUILDING, UNIT, RESIDENTIAL, SHOP

    private BigDecimal area;

    private String status;

    private String ownerName;

    private String ownerPhone;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}