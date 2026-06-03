package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.context.BaseContext;
import com.wisdom.dto.BillGenerateDTO;
import com.wisdom.dto.BillPageQueryDTO;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.PaymentAuditDTO;
import com.wisdom.dto.PaymentCreateDTO;
import com.wisdom.dto.PaymentPageQueryDTO;
import com.wisdom.entity.Bill;
import com.wisdom.entity.BillBatch;
import com.wisdom.entity.House;
import com.wisdom.entity.PaymentRecord;
import com.wisdom.exception.BusinessException;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.BillBatchMapper;
import com.wisdom.mapper.BillMapper;
import com.wisdom.mapper.PaymentRecordMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.UserService;
import com.wisdom.vo.BatchRecordVO;
import com.wisdom.vo.BillVO;
import com.wisdom.vo.PaymentVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinanceServiceImplTest {

    @Mock
    private BillBatchMapper billBatchMapper;

    @Mock
    private BillMapper billMapper;

    @Mock
    private PaymentRecordMapper paymentRecordMapper;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private FinanceServiceImpl financeService;

    @Test
    void batchGenerateBillsShouldCreateBillsForAllHouses() {
        BillGenerateDTO dto = new BillGenerateDTO();
        dto.setFeeType("物业费");
        dto.setTargetRange("全部");

        House house1 = new House();
        house1.setId(1L);
        house1.setName("1栋101");
        House house2 = new House();
        house2.setId(2L);
        house2.setName("1栋102");

        when(assetMapper.selectAllAssets()).thenReturn(List.of(house1, house2));

        BatchRecordVO result = financeService.batchGenerateBills(dto);

        // Verify batch created
        ArgumentCaptor<BillBatch> batchCaptor = ArgumentCaptor.forClass(BillBatch.class);
        verify(billBatchMapper, times(1)).insert(batchCaptor.capture());
        assertEquals("物业费", batchCaptor.getValue().getFeeType());
        assertEquals("全部", batchCaptor.getValue().getTargetRange());
        assertNotNull(batchCaptor.getValue().getBatchNo());

        // Verify 2 bills created
        verify(billMapper, times(2)).insert(any(Bill.class));

        // Verify batch updated with totals
        verify(billBatchMapper, times(1)).updateById(any(BillBatch.class));

        assertNotNull(result);
        assertEquals("物业费", result.getFeeType());
    }

    @Test
    void getBatchLogsShouldReturnMappedPage() {
        PageQueryDTO dto = new PageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);

        BillBatch batch = new BillBatch();
        batch.setId(1L);
        batch.setBatchNo("BAT-001");
        batch.setFeeType("物业费");
        batch.setStatus("COMPLETED");
        batch.setTotalCount(100);
        batch.setTotalAmount(new BigDecimal("50000"));

        Page<BillBatch> page = new Page<>(1, 10);
        page.setRecords(List.of(batch));
        page.setTotal(1);

        when(billBatchMapper.selectPage(any(Page.class), any())).thenReturn(page);

        PageResult<BatchRecordVO> result = financeService.getBatchLogs(dto);

        assertEquals(1, result.getTotal());
        assertEquals("BAT-001", result.getRecords().get(0).getBatchNo());
        assertEquals("物业费", result.getRecords().get(0).getFeeType());
    }

    @Test
    void getBillListShouldReturnMappedPageWhenAdmin() {
        BillPageQueryDTO dto = new BillPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);
        dto.setBillNo("BILL-001");

        Bill bill = new Bill();
        bill.setId(1L);
        bill.setBillNo("BILL-001");
        bill.setHouseId(100L);
        bill.setAmount(new BigDecimal("500"));
        bill.setPayStatus(0);
        bill.setType("物业费");
        bill.setDeadline(LocalDate.now().plusDays(10));

        Page<Bill> page = new Page<>(1, 10);
        page.setRecords(List.of(bill));
        page.setTotal(1);

        House house = new House();
        house.setId(100L);
        house.setName("1栋101");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(billMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(assetMapper.selectById(100L)).thenReturn(house);

        PageResult<BillVO> result = financeService.getBillList(dto);

        assertEquals(1, result.getTotal());
        BillVO vo = result.getRecords().get(0);
        assertEquals("BILL-001", vo.getBillNo());
        assertEquals("1栋101", vo.getHouseName());
        assertEquals("未缴", vo.getPayStatusText());
    }

    @Test
    void getBillListShouldReturnPaidText() {
        BillPageQueryDTO dto = new BillPageQueryDTO();
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setBillNo("BILL-002");
        bill.setHouseId(200L);
        bill.setPayStatus(1);

        Page<Bill> page = new Page<>(1, 10);
        page.setRecords(List.of(bill));
        page.setTotal(1);

        House house = new House();
        house.setId(200L);
        house.setName("2栋202");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(billMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(assetMapper.selectById(200L)).thenReturn(house);

        PageResult<BillVO> result = financeService.getBillList(dto);

        assertEquals("已缴", result.getRecords().get(0).getPayStatusText());
    }

    @Test
    void getBillListShouldFilterByOwnedHousesWhenNonAdmin() {
        BillPageQueryDTO dto = new BillPageQueryDTO();

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(2L);
        when(userService.getOwnedHouseIds(2L)).thenReturn(List.of(100L, 101L));

        Page<Bill> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);

        when(billMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        financeService.getBillList(dto);

        verify(billMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void getBillListShouldReturnEmptyWhenNonAdminHasNoHouses() {
        BillPageQueryDTO dto = new BillPageQueryDTO();

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(3L);
        when(userService.getOwnedHouseIds(3L)).thenReturn(Collections.emptyList());

        PageResult<BillVO> result = financeService.getBillList(dto);

        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());
    }

    @Test
    void auditPaymentShouldUpdatePaymentAndBill() {
        PaymentAuditDTO dto = new PaymentAuditDTO();
        dto.setStatus(1);
        dto.setOperator("财务员");

        PaymentRecord payment = new PaymentRecord();
        payment.setId(1L);
        payment.setBillId(100L);

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setPayStatus(0);

        when(paymentRecordMapper.selectById(1L)).thenReturn(payment);
        when(billMapper.selectById(100L)).thenReturn(bill);

        financeService.auditPayment(1L, dto);

        ArgumentCaptor<PaymentRecord> paymentCaptor = ArgumentCaptor.forClass(PaymentRecord.class);
        verify(paymentRecordMapper).updateById(paymentCaptor.capture());
        assertEquals(1, paymentCaptor.getValue().getStatus());
        assertEquals("财务员", paymentCaptor.getValue().getOperator());

        ArgumentCaptor<Bill> billCaptor = ArgumentCaptor.forClass(Bill.class);
        verify(billMapper).updateById(billCaptor.capture());
        assertEquals(1, billCaptor.getValue().getPayStatus());
    }

    @Test
    void auditPaymentShouldThrowWhenPaymentNotFound() {
        PaymentAuditDTO dto = new PaymentAuditDTO();
        dto.setStatus(1);

        when(paymentRecordMapper.selectById(404L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> financeService.auditPayment(404L, dto));

        verify(paymentRecordMapper, never()).updateById(any(PaymentRecord.class));
        verify(billMapper, never()).updateById(any(Bill.class));
    }

    @Test
    void createPaymentShouldCreatePaymentAndMarkBillPaid() {
        PaymentCreateDTO dto = new PaymentCreateDTO();
        dto.setBillId(100L);
        dto.setPayAmount(new BigDecimal("500"));
        dto.setPayType("微信");

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setBillNo("BILL-001");
        bill.setHouseId(200L);
        bill.setPayStatus(0);

        when(billMapper.selectById(100L)).thenReturn(bill);

        House house = new House();
        house.setId(200L);
        house.setName("1栋101");

        when(assetMapper.selectById(200L)).thenReturn(house);

        PaymentVO result = financeService.createPayment(dto);

        ArgumentCaptor<PaymentRecord> captor = ArgumentCaptor.forClass(PaymentRecord.class);
        verify(paymentRecordMapper).insert(captor.capture());
        assertEquals(100L, captor.getValue().getBillId());
        assertEquals(new BigDecimal("500"), captor.getValue().getPayAmount());
        assertEquals("微信", captor.getValue().getPayType());
        assertEquals(0, captor.getValue().getStatus());
        assertNotNull(captor.getValue().getTrxNo());

        verify(billMapper).updateById(any(Bill.class));

        assertNotNull(result);
        assertEquals("1栋101", result.getHouseName());
        assertEquals("BILL-001", result.getBillNo());
        assertEquals("待核销", result.getStatusText());
    }

    @Test
    void createPaymentShouldThrowWhenBillNotFound() {
        PaymentCreateDTO dto = new PaymentCreateDTO();
        dto.setBillId(404L);

        when(billMapper.selectById(404L)).thenReturn(null);

        assertThrows(BusinessException.class, () -> financeService.createPayment(dto));

        verify(paymentRecordMapper, never()).insert(any(PaymentRecord.class));
    }

    @Test
    void createPaymentShouldThrowWhenBillAlreadyPaid() {
        PaymentCreateDTO dto = new PaymentCreateDTO();
        dto.setBillId(100L);

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setPayStatus(1);

        when(billMapper.selectById(100L)).thenReturn(bill);

        assertThrows(BusinessException.class, () -> financeService.createPayment(dto));
    }

    @Test
    void getPaymentListShouldReturnMappedPageWhenAdmin() {
        PaymentPageQueryDTO dto = new PaymentPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);

        PaymentRecord payment = new PaymentRecord();
        payment.setId(1L);
        payment.setTrxNo("TRX-001");
        payment.setBillId(100L);
        payment.setHouseId(200L);
        payment.setPayAmount(new BigDecimal("500"));
        payment.setPayType("微信");
        payment.setStatus(0);

        Page<PaymentRecord> page = new Page<>(1, 10);
        page.setRecords(List.of(payment));
        page.setTotal(1);

        House house = new House();
        house.setId(200L);
        house.setName("1栋101");

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setBillNo("BILL-001");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(paymentRecordMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(assetMapper.selectById(200L)).thenReturn(house);
        when(billMapper.selectById(100L)).thenReturn(bill);

        PageResult<PaymentVO> result = financeService.getPaymentList(dto);

        assertEquals(1, result.getTotal());
        PaymentVO vo = result.getRecords().get(0);
        assertEquals("TRX-001", vo.getTrxNo());
        assertEquals("1栋101", vo.getHouseName());
        assertEquals("BILL-001", vo.getBillNo());
        assertEquals("待核销", vo.getStatusText());
    }

    @Test
    void getPaymentListShouldFilterByOwnedHousesWhenNonAdmin() {
        PaymentPageQueryDTO dto = new PaymentPageQueryDTO();

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(2L);
        when(userService.getOwnedHouseIds(2L)).thenReturn(List.of(200L));

        Page<PaymentRecord> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);

        when(paymentRecordMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        financeService.getPaymentList(dto);

        verify(paymentRecordMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }
}
