package com.wisdom.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AssetDTO {
    private Long id;
    private Long parentId;
    private String name;
    private String type;
    private BigDecimal area;
    private String status;
    private String ownerName;
    private String ownerPhone;
}