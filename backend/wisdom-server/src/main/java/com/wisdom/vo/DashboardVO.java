package com.wisdom.vo;

import lombok.Data;
import java.util.List;

@Data
public class DashboardVO {
    private Integer pendingChargeCount;
    private Double pendingChargeAmount;
    private Integer overdueCount;
    private Double overdueAmount;
    private Integer todayComplaintCount;
    private Integer pendingComplaintCount;
    private Integer todayRepairCount;
    private Integer pendingRepairCount;

    /** 每种资产类型的租售占比 */
    private List<AssetRentalStatus> assetRentalList;

    @Data
    public static class AssetRentalStatus {
        private String type;
        private String typeName;
        private Integer totalCount;
        private Integer vacantCount;
        private Integer rentedCount;
        private Integer soldCount;
        private Integer occupiedCount;
        private Double vacantRate;
        private Double rentedRate;
        private Double soldRate;
        private Double occupiedRate;
    }
}
