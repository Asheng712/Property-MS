package com.wisdom.controller;

import com.wisdom.dto.RepairDTO;
import com.wisdom.dto.RepairDispatchDTO;
import com.wisdom.dto.RepairStatusUpdateDTO;
import com.wisdom.result.Result;
import com.wisdom.service.RepairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repairs")
@Tag(name = "报修管理模块", description = "报修看板、工单管理等接口")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @GetMapping("/kanban")
    @Operation(summary = "获取报修看板数据")
    public Result<?> getRepairKanban() {
        return Result.success(repairService.getRepairKanban());
    }

    @PostMapping
    @Operation(summary = "录入/代录工单")
    public Result<Void> addRepair(@RequestBody RepairDTO repairDTO) {
        repairService.addRepair(repairDTO);
        return Result.success(null);
    }

    @PutMapping("/dispatch")
    @Operation(summary = "派发工单/指派师傅")
    public Result<Void> dispatchRepair(@RequestBody RepairDispatchDTO repairDispatchDTO) {
        repairService.dispatchRepair(repairDispatchDTO);
        return Result.success(null);
    }

    @PutMapping("/status")
    @Operation(summary = "更新工单状态")
    public Result<Void> updateRepairStatus(@RequestBody RepairStatusUpdateDTO repairStatusUpdateDTO) {
        repairService.updateRepairStatus(repairStatusUpdateDTO);
        return Result.success(null);
    }
}