package com.wisdom.controller;

import com.wisdom.ai.AIService;
import com.wisdom.ai.RepairAnalysisResult;
import com.wisdom.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AI辅助功能", description = "AI辅助生成公告、分析财务报表、分析报修故障")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/notice/generate")
    @Operation(summary = "AI辅助生成社区公告")
    public Result<String> generateNotice(@RequestParam String topic, @RequestParam String content) {
        String generatedNotice = aiService.generateNotice(topic, content);
        return Result.success(generatedNotice);
    }

    @PostMapping("/finance/analyze")
    @Operation(summary = "AI分析财务报表")
    public Result<String> analyzeFinancialReport(@RequestParam String reportData) {
        String analysis = aiService.analyzeFinancialReport(reportData);
        return Result.success(analysis);
    }

    @PostMapping("/repair/analyze")
    @Operation(summary = "AI分析报修故障")
    public Result<RepairAnalysisResult> analyzeRepair(@RequestParam String description) {
        RepairAnalysisResult result = aiService.analyzeRepair(description);
        return Result.success(result);
    }
}
