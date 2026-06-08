package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PropertyFeeConfigVO {
    private Long id;
    private BigDecimal unitPrice;
    private String effectiveMonth;
    private Integer status;
    private LocalDateTime createTime;
}
