package com.wisdom.vo;

import lombok.Data;
import java.util.List;

@Data
public class RepairKanbanVO {
    private List<RepairVO> pending;
    private List<RepairVO> processing;
    private List<RepairVO> completed;
}