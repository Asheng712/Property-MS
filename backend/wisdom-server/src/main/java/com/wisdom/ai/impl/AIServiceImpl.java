package com.wisdom.ai.impl;

import com.wisdom.ai.AIService;
import com.wisdom.ai.RepairAnalysisResult;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

    @Override
    public String generateNotice(String topic, String content) {
        // 模拟AI生成公告
        return String.format("【%s】\n\n各位业主：\n\n%s\n\n特此通知。\n\n物业管理处\n%s", 
                topic, content, java.time.LocalDate.now().toString());
    }

    @Override
    public String analyzeFinancialReport(String reportData) {
        // 模拟AI分析财务报表
        return "财务分析报告：\n\n1. 本月收入较上月增长10%，主要来自租金收入增加\n2. 支出方面，维修费用同比下降5%，管理费用保持稳定\n3. 建议：优化缴费提醒流程，提高缴费率\n4. 预测：下月收入预计增长8%，主要来自新租户入住\n";
    }

    @Override
    public RepairAnalysisResult analyzeRepair(String description) {
        RepairAnalysisResult result = new RepairAnalysisResult();
        
        // 简单的关键词匹配来判定故障级别
        if (description.contains("漏水") || description.contains("断电") || description.contains("电梯") || description.contains("燃气")) {
            result.setPriority(2);
            result.setPriorityText("紧急");
            result.setSuggestion("请立即联系维修人员上门处理，同时采取临时措施防止损失扩大。");
        } else {
            result.setPriority(1);
            result.setPriorityText("普通");
            result.setSuggestion("维修人员将在24小时内上门处理，请耐心等待。");
        }
        
        return result;
    }
}
