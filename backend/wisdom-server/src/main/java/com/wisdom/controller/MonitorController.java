package com.wisdom.controller;

import com.wisdom.metrics.MetricsService;
import com.wisdom.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Tag(name = "监控", description = "健康检查与系统监控")
public class MonitorController {

    @Value("${spring.application.version:1.0.0}")
    private String version;

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired
    private MetricsService metricsService;

    @GetMapping("/health")
    @Operation(summary = "健康检查")
    public Result<Map<String, Object>> health() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("status", "healthy");
        info.put("timestamp", Instant.now().toString());
        info.put("version", version);
        info.put("database", checkDatabase());
        return Result.success(info);
    }

    @GetMapping("/metrics")
    @Operation(summary = "基础指标")
    public Result<Map<String, Object>> metrics() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("totalRequests", metricsService.getTotalRequests());
        info.put("totalErrors", metricsService.getTotalErrors());
        info.put("errorRate", String.format("%.2f%%", metricsService.getErrorRate()));
        info.put("averageResponseTimeMs", String.format("%.2f", metricsService.getAverageResponseTime()));
        info.put("uptimeSeconds", metricsService.getUptimeSeconds());
        return Result.success(info);
    }

    private String checkDatabase() {
        if (dataSource == null) {
            return "unconfigured";
        }
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(3) ? "connected" : "disconnected";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
