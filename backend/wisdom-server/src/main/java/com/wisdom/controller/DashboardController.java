package com.wisdom.controller;

import com.wisdom.result.Result;
import com.wisdom.service.SystemService;
import com.wisdom.vo.DashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "仪表盘模块", description = "管理员运营看板数据接口")
public class DashboardController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/data")
    @Operation(summary = "获取管理员运营看板数据")
    public Result<DashboardVO> getDashboardData() {
        DashboardVO dashboardData = systemService.getDashboardData();
        return Result.success(dashboardData);
    }
}
