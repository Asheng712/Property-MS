package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bus_property_fee_config")
public class PropertyFeeConfig implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private BigDecimal unitPrice;
    private String effectiveMonth;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
