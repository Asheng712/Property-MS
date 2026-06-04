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
import com.wisdom.context.BaseContext;
import com.wisdom.exception.BusinessException;
import com.wisdom.service.RepairService;
import com.wisdom.service.UserService;
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

    @Autowired
    private UserService userService;

    @Override
    public RepairKanbanVO getRepairKanban() {
        return getRepairKanban(null);
    }

    @Override
    public RepairKanbanVO getRepairKanban(String reporter) {
        List<Repair> allRepairs = repairMapper.selectList(null);
        List<Repair> filtered = allRepairs;

        if (!userService.isCurrentUserAdmin()) {
            Long currentUserId = userService.getRequiredCurrentUserId();
            filtered = allRepairs.stream()
                    .filter(repair -> currentUserId.equals(repair.getReporterId()))
                    .collect(Collectors.toList());
        } else if (reporter != null && !reporter.isEmpty()) {
            filtered = allRepairs.stream()
                    .filter(repair -> reporter.equals(repair.getReporter()))
                    .collect(Collectors.toList());
        }
        List<RepairVO> pending = filtered.stream()
                .filter(repair -> repair.getStatus() == 0)
                .map(this::convertToRepairVO)
                .collect(Collectors.toList());
        List<RepairVO> processing = filtered.stream()
                .filter(repair -> repair.getStatus() == 1)
                .map(this::convertToRepairVO)
                .collect(Collectors.toList());
        List<RepairVO> completed = filtered.stream()
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
        // 确保使用数据库自增ID，清除DTO可能带入的ID值
        repair.setId(null);

        Long currentUserId = BaseContext.getCurrentId();
        if (!userService.isCurrentUserAdmin()) {
            if (currentUserId == null) {
                throw BusinessException.unauthorized();
            }
            List<Long> ownedHouseIds = userService.getOwnedHouseIds(currentUserId);
            if (!ownedHouseIds.contains(repairDTO.getHouseId())) {
                throw BusinessException.badRequest("只能对自己名下的房屋报修");
            }
        }
        repair.setReporterId(currentUserId != null ? currentUserId : repairDTO.getReporterId());
        repair.setRepairNo("REP-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        repair.setStatus(0);
        repair.setCreateTime(LocalDateTime.now());

        // 优先使用人工指定的优先级，未指定时由AI自动判定
        if (repairDTO.getPriority() != null) {
            repair.setPriority(repairDTO.getPriority());
        } else {
            RepairAnalysisResult analysisResult = aiService.analyzeRepair(repairDTO.getContent());
            repair.setPriority(analysisResult.getPriority());
        }

        repairMapper.insert(repair);
    }

    @Override
    public void dispatchRepair(RepairDispatchDTO repairDispatchDTO) {
        if (repairDispatchDTO.getId() == null) {
            throw BusinessException.badRequest("工单ID不能为空");
        }
        if (repairDispatchDTO.getWorkerId() == null) {
            throw BusinessException.badRequest("维修人员ID不能为空");
        }
        Repair repair = repairMapper.selectById(repairDispatchDTO.getId());
        if (repair == null) {
            throw new BusinessException(404, "工单不存在");
        }
        User worker = userMapper.selectById(repairDispatchDTO.getWorkerId());
        if (worker == null) {
            throw new BusinessException(404, "维修人员不存在");
        }
        repair.setWorkerId(repairDispatchDTO.getWorkerId());
        repair.setStatus(1);
        repairMapper.updateById(repair);
    }

    @Override
    public void updateRepairStatus(RepairStatusUpdateDTO repairStatusUpdateDTO) {
        if (repairStatusUpdateDTO.getId() == null) {
            throw BusinessException.badRequest("工单ID不能为空");
        }
        Repair repair = repairMapper.selectById(repairStatusUpdateDTO.getId());
        if (repair == null) {
            throw new BusinessException(404, "工单不存在");
        }
        repair.setStatus(repairStatusUpdateDTO.getStatus());
        if (repairStatusUpdateDTO.getStatus() != null && repairStatusUpdateDTO.getStatus() == 2) {
            repair.setFinishTime(LocalDateTime.now());
        }
        repairMapper.updateById(repair);
    }
}