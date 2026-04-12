package com.wisdom.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AssetVO {
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