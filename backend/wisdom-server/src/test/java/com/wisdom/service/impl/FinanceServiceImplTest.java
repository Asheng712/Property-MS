package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.BillGenerateDTO;
import com.wisdom.dto.BillPageQueryDTO;
import com.wisdom.dto.PaymentCancelDTO;
import com.wisdom.dto.PaymentPageQueryDTO;
import com.wisdom.dto.PaymentSubmitDTO;
import com.wisdom.entity.Bill;
import com.wisdom.entity.Contract;
import com.wisdom.entity.House;
import com.wisdom.entity.PaymentRecord;
import com.wisdom.entity.PaymentRecordBill;
import com.wisdom.entity.PropertyFeeConfig;
import com.wisdom.enumeration.BillStatusEnum;
import com.wisdom.enumeration.FeeTypeEnum;
import com.wisdom.exception.BusinessException;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.BillBatchMapper;
import com.wisdom.mapper.BillMapper;
import com.wisdom.mapper.ContractMapper;
import com.wisdom.mapper.PaymentRecordBillMapper;
import com.wisdom.mapper.PaymentRecordMapper;
import com.wisdom.mapper.PropertyFeeConfigMapper;
import com.wisdom.mapper.UserMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.UserService;
import com.wisdom.vo.BillVO;
import com.wisdom.vo.PaymentRecordVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FinanceServiceImplTest {

    @Mock
    private BillMapper billMapper;

    @Mock
    private PaymentRecordMapper paymentRecordMapper;

    @Mock
    private PaymentRecordBillMapper paymentRecordBillMapper;

    @Mock
    private PropertyFeeConfigMapper propertyFeeConfigMapper;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private ContractMapper contractMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BillBatchMapper billBatchMapper;

    @InjectMocks
    private FinanceServiceImpl financeService;

    // ======================== 账单生成 ========================

    @Test
    void generateBillsShouldCreateBillsForGivenHouse() {
        BillGenerateDTO dto = new BillGenerateDTO();
        dto.setFeeType(FeeTypeEnum.RENT.getCode());
        dto.setBillMonth("2026-06");
        dto.setHouseId(1L);

        House house = new House();
        house.setId(1L);
        house.setOwnerId(10L);
        house.setType("SHOP");
        house.setArea(new BigDecimal("100"));

        Contract contract = new Contract();
        contract.setId(1L);
        contract.setRentAmount(new BigDecimal("5000"));
        contract.setDeposit(new BigDecimal("10000"));

        when(assetMapper.selectById(1L)).thenReturn(house);
        when(billMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(contractMapper.selectByHouseId(1L)).thenReturn(List.of(contract));

        financeService.generateBills(dto);

        verify(billMapper).insert(any(Bill.class));
    }

    @Test
    void generateBillsShouldSkipBuildingType() {
        BillGenerateDTO dto = new BillGenerateDTO();
        dto.setFeeType(FeeTypeEnum.RENT.getCode());
        dto.setBillMonth("2026-06");
        dto.setHouseId(1L);

        House house = new House();
        house.setId(1L);
        house.setOwnerId(10L);
        house.setType("BUILDING");

        when(assetMapper.selectById(1L)).thenReturn(house);

        assertThrows(BusinessException.class, () -> financeService.generateBills(dto));
        verify(billMapper, never()).insert(any(Bill.class));
    }

    // ======================== 账单查询 ========================

    @Test
    void getBillListShouldReturnMappedPageWhenAdmin() {
        BillPageQueryDTO dto = new BillPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);

        Bill bill = new Bill();
        bill.setId(1L);
        bill.setBillNo("BILL-001");
        bill.setHouseId(100L);
        bill.setFeeType(FeeTypeEnum.RENT.getCode());
        bill.setAmount(new BigDecimal("500"));
        bill.setStatus(BillStatusEnum.PENDING.getCode());
        bill.setDueDate(LocalDate.now().plusDays(10));

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
    }

    @Test
    void getBillListShouldFilterByOwnedHousesWhenNonAdmin() {
        BillPageQueryDTO dto = new BillPageQueryDTO();

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(2L);
        when(userService.getOwnedHouseIds(2L)).thenReturn(List.of(100L));

        Page<Bill> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);

        when(billMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        financeService.getBillList(dto);

        verify(billMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    // ======================== 提交缴费 ========================

    @Test
    void submitPaymentShouldCreatePaymentAndMarkBillsSubmitted() {
        PaymentSubmitDTO dto = new PaymentSubmitDTO();
        dto.setBillIds(List.of(100L));
        dto.setPayMethod(1);
        dto.setVoucherUrl("http://img/test.jpg");

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setUserId(2L);
        bill.setBillNo("BILL-001");
        bill.setAmount(new BigDecimal("500"));
        bill.setStatus(BillStatusEnum.PENDING.getCode());

        com.wisdom.entity.User mockUser = new com.wisdom.entity.User();
        mockUser.setId(2L);
        mockUser.setUsername("testuser");
        mockUser.setRealName("测试");

        when(userService.getRequiredCurrentUserId()).thenReturn(2L);
        when(billMapper.selectBatchIds(List.of(100L))).thenReturn(List.of(bill));
        when(userMapper.selectById(2L)).thenReturn(mockUser);

        PaymentRecordVO result = financeService.submitPayment(dto);

        verify(paymentRecordMapper).insert(any(PaymentRecord.class));
        verify(paymentRecordBillMapper).insert(any(PaymentRecordBill.class));
        verify(billMapper).updateById(any(Bill.class));

        assertNotNull(result);
    }

    @Test
    void submitPaymentShouldThrowWhenBillNotFound() {
        PaymentSubmitDTO dto = new PaymentSubmitDTO();
        dto.setBillIds(List.of(404L));

        when(userService.getRequiredCurrentUserId()).thenReturn(2L);
        when(billMapper.selectBatchIds(List.of(404L))).thenReturn(List.of());

        assertThrows(BusinessException.class, () -> financeService.submitPayment(dto));

        verify(paymentRecordMapper, never()).insert(any(PaymentRecord.class));
    }

    @Test
    void submitPaymentShouldThrowWhenBillNotOwnedByUser() {
        PaymentSubmitDTO dto = new PaymentSubmitDTO();
        dto.setBillIds(List.of(100L));

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setUserId(99L); // different user
        bill.setStatus(BillStatusEnum.PENDING.getCode());

        when(userService.getRequiredCurrentUserId()).thenReturn(2L);
        when(billMapper.selectBatchIds(List.of(100L))).thenReturn(List.of(bill));

        assertThrows(BusinessException.class, () -> financeService.submitPayment(dto));
    }

    // ======================== 核销 ========================

    @Test
    void verifyPaymentShouldUpdatePaymentAndBills() {
        PaymentRecord payment = new PaymentRecord();
        payment.setId(1L);
        payment.setStatus(0); // pending

        PaymentRecordBill prb = new PaymentRecordBill();
        prb.setBillId(100L);
        prb.setPaymentRecordId(1L);

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setStatus(BillStatusEnum.SUBMITTED.getCode());

        when(paymentRecordMapper.selectById(1L)).thenReturn(payment);
        when(paymentRecordBillMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of(prb));
        when(billMapper.selectById(100L)).thenReturn(bill);

        financeService.verifyPayment(1L);

        verify(paymentRecordMapper).updateById(any(PaymentRecord.class));
        verify(billMapper).updateById(any(Bill.class));
    }

    @Test
    void verifyPaymentShouldThrowWhenNotFound() {
        when(paymentRecordMapper.selectById(404L)).thenReturn(null);

        assertThrows(BusinessException.class, () -> financeService.verifyPayment(404L));
    }

    // ======================== 撤销核销 ========================

    @Test
    void cancelPaymentShouldRevertPaymentAndBills() {
        PaymentCancelDTO dto = new PaymentCancelDTO();
        dto.setId(1L);
        dto.setCancelReason("误操作");

        PaymentRecord payment = new PaymentRecord();
        payment.setId(1L);
        payment.setStatus(1); // verified

        PaymentRecordBill prb = new PaymentRecordBill();
        prb.setBillId(100L);
        prb.setPaymentRecordId(1L);

        Bill bill = new Bill();
        bill.setId(100L);
        bill.setStatus(BillStatusEnum.PAID.getCode());

        when(paymentRecordMapper.selectById(1L)).thenReturn(payment);
        when(paymentRecordBillMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of(prb));
        when(billMapper.selectById(100L)).thenReturn(bill);

        financeService.cancelPayment(dto);

        verify(paymentRecordMapper).updateById(any(PaymentRecord.class));
        verify(billMapper).updateById(any(Bill.class));
    }

    // ======================== 支付记录查询 ========================

    @Test
    void getPaymentListShouldReturnMappedPage() {
        PaymentPageQueryDTO dto = new PaymentPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);

        PaymentRecord payment = new PaymentRecord();
        payment.setId(1L);
        payment.setPaymentNo("PAY-001");
        payment.setUserId(1L);
        payment.setAmount(new BigDecimal("500"));
        payment.setPayMethod(1);
        payment.setStatus(0);

        com.wisdom.entity.User mockUser = new com.wisdom.entity.User();
        mockUser.setId(1L);
        mockUser.setUsername("test");
        mockUser.setRealName("测试");

        Page<PaymentRecord> page = new Page<>(1, 10);
        page.setRecords(List.of(payment));
        page.setTotal(1);

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(paymentRecordMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(paymentRecordBillMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of());
        when(userMapper.selectById(1L)).thenReturn(mockUser);

        PageResult<PaymentRecordVO> result = financeService.getPaymentList(dto);

        assertEquals(1, result.getTotal());
        PaymentRecordVO vo = result.getRecords().get(0);
        assertEquals("PAY-001", vo.getPaymentNo());
    }

    // ======================== 物业费配置 ========================

    @Test
    void setPropertyFeeConfigShouldInsertConfig() {
        com.wisdom.dto.PropertyFeeConfigDTO dto = new com.wisdom.dto.PropertyFeeConfigDTO();
        dto.setUnitPrice(new BigDecimal("3.50"));

        financeService.setPropertyFeeConfig(dto);

        verify(propertyFeeConfigMapper).insert(any(PropertyFeeConfig.class));
    }

    @Test
    void getCurrentPropertyFeeConfigShouldReturnLatest() {
        PropertyFeeConfig config = new PropertyFeeConfig();
        config.setId(1L);
        config.setUnitPrice(new BigDecimal("2.00"));
        config.setEffectiveMonth("2026-06");

        when(propertyFeeConfigMapper.getEffectiveConfig(any(String.class))).thenReturn(config);

        var result = financeService.getCurrentPropertyFeeConfig();

        assertNotNull(result);
        assertEquals(new BigDecimal("2.00"), result.getUnitPrice());
    }
}
