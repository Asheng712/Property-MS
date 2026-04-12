package com.wisdom.service;

import com.wisdom.dto.RepairDTO;
import com.wisdom.dto.RepairDispatchDTO;
import com.wisdom.dto.RepairStatusUpdateDTO;
import com.wisdom.vo.RepairKanbanVO;

public interface RepairService {
    RepairKanbanVO getRepairKanban();
    void addRepair(RepairDTO repairDTO);
    void dispatchRepair(RepairDispatchDTO repairDispatchDTO);
    void updateRepairStatus(RepairStatusUpdateDTO repairStatusUpdateDTO);
}