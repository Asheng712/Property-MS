package com.wisdom.controller;

import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.ReportExportDTO;
import com.wisdom.result.Result;
import com.wisdom.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/system")
@Tag(name = "系统管理模块", description = "导入导出任务、报表导出等接口")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/tasks")
    @Operation(summary = "获取导入导出任务")
    public Result<?> getFileTasks(PageQueryDTO pageQueryDTO) {
        return Result.success(systemService.getFileTasks(pageQueryDTO));
    }

    @GetMapping("/export/finance")
    @Operation(summary = "导出财务报表")
    public Result<Void> exportFinanceReport(ReportExportDTO reportExportDTO) {
        systemService.exportFinanceReport(reportExportDTO);
        return Result.success(null);
    }

    @PostMapping("/import/assets")
    @Operation(summary = "导入资产数据")
    public Result<Void> importAssets(@RequestParam("file") MultipartFile file) {
        systemService.importAssets(file);
        return Result.success(null);
    }
}