package com.wisdom.vo;

import lombok.Data;

@Data
public class DashboardVO {
    // 待收费数据
    private Integer pendingChargeCount;
    private Double pendingChargeAmount;
    
    // 欠费数据
    private Integer overdueCount;
    private Double overdueAmount;
    
    // 投诉数据
    private Integer todayComplaintCount;
    private Integer pendingComplaintCount;
    
    // 报修数据
    private Integer todayRepairCount;
    private Integer pendingRepairCount;
    
    // 商铺租售情况
    private ShopRentalStatus shopRentalStatus;
    
    // 车位租售情况
    private ParkingRentalStatus parkingRentalStatus;
    
    @Data
    public static class ShopRentalStatus {
        private Double vacantRate;
        private Double rentedRate;
        private Double soldRate;
    }
    
    @Data
    public static class ParkingRentalStatus {
        private Double vacantRate;
        private Double rentedRate;
        private Double soldRate;
    }
}
