package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wisdom.dto.RepairDTO;
import com.wisdom.dto.RepairDispatchDTO;
import com.wisdom.dto.RepairStatusUpdateDTO;
import com.wisdom.entity.Repair;
import com.wisdom.entity.House;
import com.wisdom.entity.User;
import com.wisdom.mapper.RepairMapper;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.UserMapper;
import com.wisdom.ai.AIService;
import com.wisdom.ai.RepairAnalysisResult;
import com.wisdom.service.RepairService;
import com.wisdom.vo.RepairKanbanVO;
import com.wisdom.vo.RepairVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AIService aiService;

    @Override
    public RepairKanbanVO getRepairKanban() {
        List<Repair> allRepairs = repairMapper.selectList(null);
        List<RepairVO> pending = allRepairs.stream()
                .filter(repair -> repair.getStatus() == 0)
                .map(this::convertToRepairVO)
                .collect(Collectors.toList());
        List<RepairVO> processing = allRepairs.stream()
                .filter(repair -> repair.getStatus() == 1)
                .map(this::convertToRepairVO)
                .collect(Collectors.toList());
        List<RepairVO> completed = allRepairs.stream()
                .filter(repair -> repair.getStatus() == 2)
                .map(this::convertToRepairVO)
                .collect(Collectors.toList());
        RepairKanbanVO kanbanVO = new RepairKanbanVO();
        kanbanVO.setPending(pending);
        kanbanVO.setProcessing(processing);
        kanbanVO.setCompleted(completed);
        return kanbanVO;
    }

    private RepairVO convertToRepairVO(Repair repair) {
        RepairVO repairVO = new RepairVO();
        BeanUtils.copyProperties(repair, repairVO);
        House house = assetMapper.selectById(repair.getHouseId());
        if (house != null) {
            repairVO.setHouseName(house.getName());
        }
        User worker = userMapper.selectById(repair.getWorkerId());
        if (worker != null) {
            repairVO.setWorkerName(worker.getUsername());
        }
        repairVO.setStatusText(getStatusText(repair.getStatus()));
        repairVO.setPriorityText(getPriorityText(repair.getPriority()));
        return repairVO;
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待处理";
            case 1: return "处理中";
            case 2: return "已办结";
            default: return "未知";
        }
    }

    private String getPriorityText(Integer priority) {
        switch (priority) {
            case 1: return "普通";
            case 2: return "紧急";
            default: return "未知";
        }
    }

    @Override
    public void addRepair(RepairDTO repairDTO) {
        Repair repair = new Repair();
        BeanUtils.copyProperties(repairDTO, repair);
        repair.setRepairNo("REP-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        repair.setStatus(0);
        repair.setCreateTime(LocalDateTime.now());
        
        // 使用AI分析故障级别
        RepairAnalysisResult analysisResult = aiService.analyzeRepair(repairDTO.getContent());
        repair.setPriority(analysisResult.getPriority());
        
        repairMapper.insert(repair);
    }

    @Override
    public void dispatchRepair(RepairDispatchDTO repairDispatchDTO) {
        Repair repair = repairMapper.selectById(repairDispatchDTO.getId());
        if (repair == null) {
            throw new RuntimeException("工单不存在");
        }
        repair.setWorkerId(repairDispatchDTO.getWorkerId());
        repair.setStatus(1);
        repairMapper.updateById(repair);
    }

    @Override
    public void updateRepairStatus(RepairStatusUpdateDTO repairStatusUpdateDTO) {
        Repair repair = repairMapper.selectById(repairStatusUpdateDTO.getId());
        if (repair == null) {
            throw new RuntimeException("工单不存在");
        }
        repair.setStatus(repairStatusUpdateDTO.getStatus());
        if (repairStatusUpdateDTO.getStatus() == 2) {
            repair.setFinishTime(LocalDateTime.now());
        }
        repairMapper.updateById(repair);
    }
}