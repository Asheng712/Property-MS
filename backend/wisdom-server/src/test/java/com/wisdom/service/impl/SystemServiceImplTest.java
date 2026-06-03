package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.wisdom.vo.DashboardVO;
import com.wisdom.vo.FileTaskVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemServiceImplTest {

    @Mock
    private FileTaskMapper fileTaskMapper;

    @Mock
    private BillMapper billMapper;

    @Mock
    private ComplaintMapper complaintMapper;

    @Mock
    private RepairMapper repairMapper;

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private SystemServiceImpl systemService;

    @Test
    void getFileTasksShouldReturnMappedPage() {
        PageQueryDTO dto = new PageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);

        FileTask task = new FileTask();
        task.setId(1L);
        task.setTaskType("EXPORT");
        task.setFileName("报表.xlsx");
        task.setStatus("SUCCESS");
        task.setOperator("admin");

        Page<FileTask> page = new Page<>(1, 10);
        page.setRecords(List.of(task));
        page.setTotal(1);

        when(fileTaskMapper.selectPage(any(Page.class), isNull())).thenReturn(page);

        PageResult<FileTaskVO> result = systemService.getFileTasks(dto);

        assertEquals(1, result.getTotal());
        assertEquals("EXPORT", result.getRecords().get(0).getTaskType());
        assertEquals("报表.xlsx", result.getRecords().get(0).getFileName());
    }

    @Test
    void exportFinanceReportShouldCreateTask() {
        ReportExportDTO dto = new ReportExportDTO();

        systemService.exportFinanceReport(dto);

        ArgumentCaptor<FileTask> captor = ArgumentCaptor.forClass(FileTask.class);
        verify(fileTaskMapper, times(1)).insert(captor.capture());
        assertEquals("EXPORT", captor.getValue().getTaskType());
        assertNotNull(captor.getValue().getFileName());

        verify(fileTaskMapper, times(1)).updateById(any(FileTask.class));
    }

    @Test
    void importAssetsShouldCreateTask() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "assets.xlsx", "application/vnd.ms-excel", "fake".getBytes());

        systemService.importAssets(file);

        ArgumentCaptor<FileTask> captor = ArgumentCaptor.forClass(FileTask.class);
        verify(fileTaskMapper, times(1)).insert(captor.capture());
        assertEquals("IMPORT", captor.getValue().getTaskType());
        assertEquals("assets.xlsx", captor.getValue().getFileName());

        verify(fileTaskMapper, times(1)).updateById(any(FileTask.class));
    }

    @Test
    void getDashboardDataShouldReturnDashboardStats() {
        // Pending charge bills
        Bill pendingBill = new Bill();
        pendingBill.setId(1L);
        pendingBill.setAmount(new BigDecimal("500"));
        pendingBill.setPayStatus(0);
        pendingBill.setDeadline(LocalDate.now().plusDays(10));

        // Overdue bill
        Bill overdueBill = new Bill();
        overdueBill.setId(2L);
        overdueBill.setAmount(new BigDecimal("300"));
        overdueBill.setPayStatus(0);
        overdueBill.setDeadline(LocalDate.now().minusDays(5));

        // Complaint
        Complaint complaint = new Complaint();
        complaint.setId(1L);
        complaint.setStatus(0);

        // Repair
        Repair repair = new Repair();
        repair.setId(1L);
        repair.setStatus(0);

        // Houses
        House residentialRenting = new House();
        residentialRenting.setId(1L);
        residentialRenting.setType("RESIDENTIAL");
        residentialRenting.setName("1栋101");
        residentialRenting.setStatus("RENTING");

        House residentialVacant = new House();
        residentialVacant.setId(2L);
        residentialVacant.setType("RESIDENTIAL");
        residentialVacant.setName("1栋102");
        residentialVacant.setStatus("VACANT");

        when(billMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(
                List.of(pendingBill),                   // first call: pending
                List.of(overdueBill)                     // second call: overdue
        );
        when(complaintMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(3L, 1L);
        when(repairMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(2L, 2L);
        when(assetMapper.selectList(isNull())).thenReturn(List.of(residentialRenting, residentialVacant));

        DashboardVO result = systemService.getDashboardData();

        assertNotNull(result);
        assertEquals(1, result.getPendingChargeCount());
        assertEquals(500.0, result.getPendingChargeAmount(), 0.01);
        assertEquals(1, result.getOverdueCount());
        assertEquals(300.0, result.getOverdueAmount(), 0.01);
        assertEquals(3, result.getTodayComplaintCount());
        assertEquals(1, result.getPendingComplaintCount());
        assertEquals(2, result.getTodayRepairCount());
        assertEquals(2, result.getPendingRepairCount());
        assertNotNull(result.getAssetRentalList());
        assertEquals(4, result.getAssetRentalList().size());
    }
}
