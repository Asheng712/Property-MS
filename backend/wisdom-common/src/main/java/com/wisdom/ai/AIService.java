package com.wisdom.ai;

public interface AIService {
    /**
     * AI辅助生成社区公告
     * @param topic 公告主题
     * @param content 公告内容要点
     * @return 生成的公告内容
     */
    String generateNotice(String topic, String content);

    /**
     * AI分析财务报表
     * @param reportData 报表数据
     * @return 分析结果
     */
    String analyzeFinancialReport(String reportData);

    /**
     * AI判定故障级别并给出维修建议
     * @param description 故障描述
     * @return 故障分析结果
     */
    RepairAnalysisResult analyzeRepair(String description);
}
