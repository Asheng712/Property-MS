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
        pendingChargeWrapper.eq(Bill::getStatus, 0);
        List<Bill> pendingChargeBills = billMapper.selectList(pendingChargeWrapper);
        dashboardVO.setPendingChargeCount(pendingChargeBills.size());
        double pendingChargeAmount = pendingChargeBills.stream()
                .mapToDouble(bill -> bill.getAmount() != null ? bill.getAmount().doubleValue() : 0)
                .sum();
        dashboardVO.setPendingChargeAmount(pendingChargeAmount);
        
        // 获取欠费数据（假设deadline小于当前日期且未缴费）
        LambdaQueryWrapper<Bill> overdueWrapper = new LambdaQueryWrapper<>();
        overdueWrapper.eq(Bill::getStatus, 0)
                .lt(Bill::getDueDate, LocalDate.now());
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
        
        // 获取每种资产类型的租售占比
        List<House> allHouses = assetMapper.selectList(null);
        java.util.Map<String, String> typeNameMap = java.util.Map.of(
            "SHOP", "商铺",
            "RESIDENTIAL", "住宅",
            "BUILDING", "楼栋",
            "UNIT", "单元"
        );
        java.util.List<DashboardVO.AssetRentalStatus> assetRentalList = new java.util.ArrayList<>();
        for (String type : java.util.List.of("SHOP", "RESIDENTIAL", "BUILDING", "UNIT")) {
            java.util.List<House> typeHouses = allHouses.stream()
                .filter(h -> type.equals(h.getType()))
                .collect(java.util.stream.Collectors.toList());
            DashboardVO.AssetRentalStatus status = new DashboardVO.AssetRentalStatus();
            status.setType(type);
            status.setTypeName(typeNameMap.getOrDefault(type, type));
            status.setTotalCount(typeHouses.size());
            status.setVacantCount((int) typeHouses.stream().filter(h -> "VACANT".equals(h.getStatus())).count());
            status.setRentedCount((int) typeHouses.stream().filter(h -> "RENTING".equals(h.getStatus())).count());
            status.setSoldCount((int) typeHouses.stream().filter(h -> "SOLD".equals(h.getStatus())).count());
            status.setOccupiedCount((int) typeHouses.stream().filter(h -> "OCCUPIED".equals(h.getStatus())).count());
            int total = status.getTotalCount();
            status.setVacantRate(total > 0 ? (double) status.getVacantCount() / total * 100 : 0);
            status.setRentedRate(total > 0 ? (double) status.getRentedCount() / total * 100 : 0);
            status.setSoldRate(total > 0 ? (double) status.getSoldCount() / total * 100 : 0);
            status.setOccupiedRate(total > 0 ? (double) status.getOccupiedCount() / total * 100 : 0);
            assetRentalList.add(status);
        }
        dashboardVO.setAssetRentalList(assetRentalList);

        return dashboardVO;
    }
}