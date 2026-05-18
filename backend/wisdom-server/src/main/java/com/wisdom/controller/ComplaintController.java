package com.wisdom.controller;

import com.wisdom.dto.ComplaintCreateDTO;
import com.wisdom.dto.ComplaintHandleDTO;
import com.wisdom.dto.ComplaintPageQueryDTO;
import com.wisdom.result.Result;
import com.wisdom.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/complaints")
@Tag(name = "投诉管理模块", description = "投诉建议查询、提交、处理等接口")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping
    @Operation(summary = "分页查询投诉建议")
    public Result<?> getComplaintList(ComplaintPageQueryDTO complaintPageQueryDTO) {
        return Result.success(complaintService.getComplaintList(complaintPageQueryDTO));
    }

    @PostMapping
    @Operation(summary = "提交投诉建议")
    public Result<Void> createComplaint(@Valid @RequestBody ComplaintCreateDTO complaintCreateDTO) {
        complaintService.createComplaint(complaintCreateDTO);
        return Result.success(null);
    }

    @PutMapping("/handle")
    @Operation(summary = "处理并反馈投诉")
    public Result<Void> handleComplaint(@RequestBody ComplaintHandleDTO complaintHandleDTO) {
        complaintService.handleComplaint(complaintHandleDTO);
        return Result.success(null);
    }
}