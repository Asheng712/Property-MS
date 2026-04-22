package com.wisdom.ai;

import lombok.Data;

@Data
public class RepairAnalysisResult {
    /**
     * 故障级别：1-普通，2-紧急
     */
    private Integer priority;

    /**
     * 故障级别描述
     */
    private String priorityText;

    /**
     * 维修建议
     */
    private String suggestion;
}
