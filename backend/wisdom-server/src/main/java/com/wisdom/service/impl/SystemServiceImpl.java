package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.ReportExportDTO;
import com.wisdom.entity.Bill;
import com.wisdom.entity.Complaint;
import com.wisdom.entity.FileTask;
import com.wisdom.entity.House;
import com.wisdom.entity.Repair;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.BillMapper;
import com.wisdom.mapper.ComplaintMapper;
import com.wisdom.mapper.FileTaskMapper;
import com.wisdom.mapper.RepairMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.SystemService;
import com.wisdom.vo.DashboardVO;
import com.wisdom.vo.FileTaskVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private FileTaskMapper fileTaskMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Override
    public PageResult<FileTaskVO> getFileTasks(PageQueryDTO pageQueryDTO) {
        int pageNum = pageQueryDTO.getPage() != null ? pageQueryDTO.getPage() : 1;
        int pageSize = pageQueryDTO.getPageSize() != null ? pageQueryDTO.getPageSize() : 10;
        Page<FileTask> page = new Page<>(pageNum, pageSize);
        IPage<FileTask> fileTaskPage = fileTaskMapper.selectPage(page, null);
        List<FileTaskVO> fileTaskVOList = fileTaskPage.getRecords().stream()
                .map(fileTask -> {
                    FileTaskVO fileTaskVO = new FileTaskVO();
                    BeanUtils.copyProperties(fileTask, fileTaskVO);
                    return fileTaskVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(fileTaskPage.getTotal(), fileTaskVOList);
    }

    @Override
    public void exportFinanceReport(ReportExportDTO reportExportDTO) {
        FileTask fileTask = new FileTask();
        fileTask.setTaskType("EXPORT");
        fileTask.setFileName("财务报表_" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx");
        fileTask.setOperator("admin");
        fileTask.setStatus("PROCESSING");
        fileTask.setCreateTime(LocalDateTime.now());
        fileTaskMapper.insert(fileTask);
        
        fileTask.setStatus("SUCCESS");
        fileTask.setFileUrl("/downloads/finance_report.xlsx");
        fileTaskMapper.updateById(fileTask);
    }

    @Override
    public void importAssets(MultipartFile file) {
        FileTask fileTask = new FileTask();
        fileTask.setTaskType("IMPORT");
        fileTask.setFileName(file.getOriginalFilename());
        fileTask.setOperator("admin");
        fileTask.setStatus("PROCESSING");
        fileTask.setCreateTime(LocalDateTime.now());
        fileTaskMapper.insert(fileTask);
        
        fileTask.setStatus("SUCCESS");
        fileTask.setDataCount(10);
        fileTaskMapper.updateById(fileTask);
    }

    @Override
    public DashboardVO getDashboardData() {
        DashboardVO dashboardVO = new DashboardVO();
        
        // 获取待收费数据
        LambdaQueryWrapper<Bill> pendingChargeWrapper = new LambdaQueryWrapper<>();
        pendingChargeWrapper.eq(Bill::getPayStatus, 0);
        List<Bill> pendingChargeBills = billMapper.selectList(pendingChargeWrapper);
        dashboardVO.setPendingChargeCount(pendingChargeBills.size());
        double pendingChargeAmount = pendingChargeBills.stream()
                .mapToDouble(bill -> bill.getAmount() != null ? bill.getAmount().doubleValue() : 0)
                .sum();
        dashboardVO.setPendingChargeAmount(pendingChargeAmount);
        
        // 获取欠费数据（假设deadline小于当前日期且未缴费）
        LambdaQueryWrapper<Bill> overdueWrapper = new LambdaQueryWrapper<>();
        overdueWrapper.eq(Bill::getPayStatus, 0)
                .lt(Bill::getDeadline, LocalDate.now());
        List<Bill> overdueBills = billMapper.selectList(overdueWrapper);
        dashboardVO.setOverdueCount(overdueBills.size());
        double overdueAmount = overdueBills.stream()
                .mapToDouble(bill -> bill.getAmount() != null ? bill.getAmount().doubleValue() : 0)
                .sum();
        dashboardVO.setOverdueAmount(overdueAmount);
        
        // 获取今日投诉数据
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LambdaQueryWrapper<Complaint> todayComplaintWrapper = new LambdaQueryWrapper<>();
        todayComplaintWrapper.ge(Complaint::getCreateTime, todayStart);
        dashboardVO.setTodayComplaintCount(complaintMapper.selectCount(todayComplaintWrapper).intValue());
        
        // 获取待办投诉数据
        LambdaQueryWrapper<Complaint> pendingComplaintWrapper = new LambdaQueryWrapper<>();
        pendingComplaintWrapper.eq(Complaint::getStatus, 0);
        dashboardVO.setPendingComplaintCount(complaintMapper.selectCount(pendingComplaintWrapper).intValue());
        
        // 获取今日报修数据
        LambdaQueryWrapper<Repair> todayRepairWrapper = new LambdaQueryWrapper<>();
        todayRepairWrapper.ge(Repair::getCreateTime, todayStart);
        dashboardVO.setTodayRepairCount(repairMapper.selectCount(todayRepairWrapper).intValue());
        
        // 获取待办报修数据
        LambdaQueryWrapper<Repair> pendingRepairWrapper = new LambdaQueryWrapper<>();
        pendingRepairWrapper.eq(Repair::getStatus, 0);
        dashboardVO.setPendingRepairCount(repairMapper.selectCount(pendingRepairWrapper).intValue());
        
        // 获取商铺租售情况
        DashboardVO.ShopRentalStatus shopRentalStatus = new DashboardVO.ShopRentalStatus();
        LambdaQueryWrapper<House> shopWrapper = new LambdaQueryWrapper<>();
        shopWrapper.eq(House::getType, "SHOP");
        List<House> shops = assetMapper.selectList(shopWrapper);
        int shopTotal = shops.size();
        int shopVacant = (int) shops.stream().filter(shop -> "VACANT".equals(shop.getStatus())).count();
        int shopRented = (int) shops.stream().filter(shop -> "RENTING".equals(shop.getStatus())).count();
        int shopSold = (int) shops.stream().filter(shop -> "SOLD".equals(shop.getStatus())).count();
        
        shopRentalStatus.setVacantRate(shopTotal > 0 ? (double) shopVacant / shopTotal * 100 : 0);
        shopRentalStatus.setRentedRate(shopTotal > 0 ? (double) shopRented / shopTotal * 100 : 0);
        shopRentalStatus.setSoldRate(shopTotal > 0 ? (double) shopSold / shopTotal * 100 : 0);
        dashboardVO.setShopRentalStatus(shopRentalStatus);
        
        // 获取车位租售情况（假设车位也是House的一种类型）
        DashboardVO.ParkingRentalStatus parkingRentalStatus = new DashboardVO.ParkingRentalStatus();
        LambdaQueryWrapper<House> parkingWrapper = new LambdaQueryWrapper<>();
        parkingWrapper.eq(House::getType, "PARKING");
        List<House> parkings = assetMapper.selectList(parkingWrapper);
        int parkingTotal = parkings.size();
        int parkingVacant = (int) parkings.stream().filter(parking -> "VACANT".equals(parking.getStatus())).count();
        int parkingRented = (int) parkings.stream().filter(parking -> "RENTING".equals(parking.getStatus())).count();
        int parkingSold = (int) parkings.stream().filter(parking -> "SOLD".equals(parking.getStatus())).count();
        
        parkingRentalStatus.setVacantRate(parkingTotal > 0 ? (double) parkingVacant / parkingTotal * 100 : 0);
        parkingRentalStatus.setRentedRate(parkingTotal > 0 ? (double) parkingRented / parkingTotal * 100 : 0);
        parkingRentalStatus.setSoldRate(parkingTotal > 0 ? (double) parkingSold / parkingTotal * 100 : 0);
        dashboardVO.setParkingRentalStatus(parkingRentalStatus);
        
        return dashboardVO;
    }
}