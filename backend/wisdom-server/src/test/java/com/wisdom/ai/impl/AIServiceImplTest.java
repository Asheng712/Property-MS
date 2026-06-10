package com.wisdom.ai.impl;

import com.wisdom.ai.RepairAnalysisResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AIServiceImplTest {

    private AIServiceImpl aiService;

    @BeforeEach
    void setUp() {
        aiService = new AIServiceImpl();
    }

    @Test
    void generateNoticeShouldContainTopicAndContent() {
        String result = aiService.generateNotice("停水通知", "明天上午8点到下午5点停水");

        assertNotNull(result);
        assertTrue(result.contains("停水通知"));
        assertTrue(result.contains("明天上午8点到下午5点停水"));
        assertTrue(result.contains("物业管理处"));
        assertTrue(result.contains("各位业主"));
    }

    @Test
    void generateNoticeShouldIncludeCurrentDate() {
        String result = aiService.generateNotice("电梯维护", "电梯进行例行维护");

        String expectedDate = java.time.LocalDate.now().toString();
        assertTrue(result.contains(expectedDate));
    }

    @Test
    void analyzeFinancialReportShouldReturnAnalysis() {
        String reportData = "本月收入100万，支出80万";
        String result = aiService.analyzeFinancialReport(reportData);

        assertNotNull(result);
        assertTrue(result.contains("财务分析报告"));
        assertTrue(result.contains("收入"));
        assertTrue(result.contains("支出"));
    }

    @Test
    void analyzeRepairShouldReturnEmergencyForWaterLeak() {
        RepairAnalysisResult result = aiService.analyzeRepair("厨房漏水严重");

        assertNotNull(result);
        assertEquals(2, result.getPriority());
        assertEquals("紧急", result.getPriorityText());
        assertTrue(result.getSuggestion().contains("立即"));
    }

    @Test
    void analyzeRepairShouldReturnEmergencyForPowerOutage() {
        RepairAnalysisResult result = aiService.analyzeRepair("整栋楼断电");

        assertEquals(2, result.getPriority());
        assertEquals("紧急", result.getPriorityText());
    }

    @Test
    void analyzeRepairShouldReturnEmergencyForElevator() {
        RepairAnalysisResult result = aiService.analyzeRepair("电梯故障无法运行");

        assertEquals(2, result.getPriority());
        assertEquals("紧急", result.getPriorityText());
    }

    @Test
    void analyzeRepairShouldReturnEmergencyForGas() {
        RepairAnalysisResult result = aiService.analyzeRepair("燃气管道泄漏");

        assertEquals(2, result.getPriority());
        assertEquals("紧急", result.getPriorityText());
    }

    @Test
    void analyzeRepairShouldReturnNormalForMinorIssues() {
        RepairAnalysisResult result = aiService.analyzeRepair("墙面需要粉刷");

        assertEquals(1, result.getPriority());
        assertEquals("普通", result.getPriorityText());
        assertTrue(result.getSuggestion().contains("24小时内"));
    }

    @Test
    void generateNoticeShouldHandleEmptyContent() {
        String result = aiService.generateNotice("测试", "");

        assertNotNull(result);
        assertTrue(result.contains("测试"));
    }
}
