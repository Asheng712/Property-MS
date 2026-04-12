package com.wisdom.vo;

import lombok.Data;
import java.util.List;

@Data
public class AssetTreeVO {
    private Long id;
    private String name;
    private String type;
    private List<AssetTreeVO> children;
}