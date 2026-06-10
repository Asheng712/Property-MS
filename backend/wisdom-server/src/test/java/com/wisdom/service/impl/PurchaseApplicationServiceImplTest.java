package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.context.BaseContext;
import com.wisdom.dto.PurchaseApplicationDTO;
import com.wisdom.dto.PurchaseApplicationPageQueryDTO;
import com.wisdom.dto.PurchaseApprovalDTO;
import com.wisdom.entity.Contract;
import com.wisdom.entity.House;
import com.wisdom.entity.PurchaseApplication;
import com.wisdom.entity.User;
import com.wisdom.exception.BusinessException;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.ContractMapper;
import com.wisdom.mapper.PurchaseApplicationMapper;
import com.wisdom.mapper.UserMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.FinanceService;
import com.wisdom.service.UserService;
import com.wisdom.vo.PurchaseApplicationVO;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseApplicationServiceImplTest {

    @Mock
    private PurchaseApplicationMapper purchaseApplicationMapper;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private ContractMapper contractMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @Mock
    private FinanceService financeService;

    @InjectMocks
    private PurchaseApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        BaseContext.setCurrentId(1L);
    }

    @AfterEach
    void tearDown() {
        BaseContext.removeCurrentId();
    }

    // --- getList tests ---

    @Test
    void getListShouldReturnPagedApplicationsWithFullFilters() {
        PurchaseApplicationPageQueryDTO dto = new PurchaseApplicationPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);
        dto.setStatus(0);
        dto.setApplicantName("张三");
        dto.setHouseId(100L);
        dto.setType("PURCHASE");

        when(userService.isCurrentUserAdmin()).thenReturn(true);

        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setApplicationNo("APP-20240101120000");
        app.setType("PURCHASE");
        app.setHouseId(100L);
        app.setApplicantId(1L);
        app.setApplicantName("张三");
        app.setStatus(0);

        Page<PurchaseApplication> page = new Page<>(1, 10);
        page.setRecords(List.of(app));
        page.setTotal(1);

        when(purchaseApplicationMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        House house = new House();
        house.setId(100L);
        house.setName("1栋101");
        when(assetMapper.selectById(100L)).thenReturn(house);

        PageResult<PurchaseApplicationVO> result = service.getList(dto);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        PurchaseApplicationVO vo = result.getRecords().get(0);
        assertEquals(1L, vo.getId());
        assertEquals("1栋101", vo.getHouseName());
        assertEquals("待审批", vo.getStatusText());
    }

    @Test
    void getListShouldUseDefaultPaginationWhenNull() {
        PurchaseApplicationPageQueryDTO dto = new PurchaseApplicationPageQueryDTO();
        when(userService.isCurrentUserAdmin()).thenReturn(true);

        Page<PurchaseApplication> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);
        when(purchaseApplicationMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        PageResult<PurchaseApplicationVO> result = service.getList(dto);

        assertEquals(0, result.getTotal());
        ArgumentCaptor<Page<PurchaseApplication>> pageCaptor = ArgumentCaptor.forClass(Page.class);
        verify(purchaseApplicationMapper).selectPage(pageCaptor.capture(), any(LambdaQueryWrapper.class));
        assertEquals(1, pageCaptor.getValue().getCurrent());
        assertEquals(10, pageCaptor.getValue().getSize());
    }

    @Test
    void getListShouldFilterByCurrentUserWhenNotAdmin() {
        PurchaseApplicationPageQueryDTO dto = new PurchaseApplicationPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(1L);

        Page<PurchaseApplication> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);
        when(purchaseApplicationMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(page);

        service.getList(dto);

        verify(userService).getRequiredCurrentUserId();
    }

    // --- getById tests ---

    @Test
    void getByIdShouldReturnMappedVOWhenApplicationExists() {
        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setApplicationNo("APP-20240101120000");
        app.setType("PURCHASE");
        app.setHouseId(100L);
        app.setApplicantId(1L);
        app.setApplicantName("张三");
        app.setStatus(0);

        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);

        House house = new House();
        house.setId(100L);
        house.setName("1栋101");
        when(assetMapper.selectById(100L)).thenReturn(house);

        PurchaseApplicationVO vo = service.getById(1L);

        assertNotNull(vo);
        assertEquals(1L, vo.getId());
        assertEquals("1栋101", vo.getHouseName());
        assertEquals("待审批", vo.getStatusText());
    }

    @Test
    void getByIdShouldThrowNotFoundWhenApplicationDoesNotExist() {
        when(purchaseApplicationMapper.selectById(999L)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.getById(999L));

        assertEquals("申请不存在", ex.getMessage());
    }

    @Test
    void getByIdShouldNotSetHouseNameWhenHouseNotFound() {
        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setHouseId(100L);
        app.setStatus(0);

        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);
        when(assetMapper.selectById(100L)).thenReturn(null);

        PurchaseApplicationVO vo = service.getById(1L);

        assertNotNull(vo);
        assertNull(vo.getHouseName());
    }

    // --- create tests ---

    @Test
    void createShouldInsertApplicationWhenDataIsValid() {
        User user = new User();
        user.setId(1L);
        user.setRealName("张三");
        user.setPhone("13800000001");
        when(userMapper.selectById(1L)).thenReturn(user);

        PurchaseApplicationDTO dto = new PurchaseApplicationDTO();
        dto.setType("PURCHASE");
        dto.setHouseId(100L);
        dto.setApplicantName("张三");
        dto.setApplicantPhone("13800000001");
        dto.setRemark("申请购买");

        service.create(dto);

        ArgumentCaptor<PurchaseApplication> captor = ArgumentCaptor.forClass(PurchaseApplication.class);
        verify(purchaseApplicationMapper).insert(captor.capture());

        PurchaseApplication saved = captor.getValue();
        assertEquals("PURCHASE", saved.getType());
        assertEquals(Long.valueOf(100L), saved.getHouseId());
        assertEquals(Long.valueOf(1L), saved.getApplicantId());
        assertEquals("张三", saved.getApplicantName());
        assertEquals(0, saved.getStatus());
        assertNotNull(saved.getApplicationNo());
    }

    @Test
    void createShouldDefaultToPurchaseTypeWhenNotSpecified() {
        User user = new User();
        user.setId(1L);
        user.setRealName("张三");
        user.setPhone("13800000001");
        when(userMapper.selectById(1L)).thenReturn(user);

        PurchaseApplicationDTO dto = new PurchaseApplicationDTO();
        dto.setHouseId(100L);

        service.create(dto);

        ArgumentCaptor<PurchaseApplication> captor = ArgumentCaptor.forClass(PurchaseApplication.class);
        verify(purchaseApplicationMapper).insert(captor.capture());
        assertEquals("PURCHASE", captor.getValue().getType());
    }

    @Test
    void createShouldThrowWhenUserNotFound() {
        when(userMapper.selectById(1L)).thenReturn(null);

        PurchaseApplicationDTO dto = new PurchaseApplicationDTO();
        dto.setHouseId(100L);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(dto));

        assertEquals("UNAUTHORIZED", ex.getMessage());
    }

    @Test
    void createShouldThrowWhenTypeIsInvalid() {
        User user = new User();
        user.setId(1L);
        when(userMapper.selectById(1L)).thenReturn(user);

        PurchaseApplicationDTO dto = new PurchaseApplicationDTO();
        dto.setType("INVALID_TYPE");
        dto.setHouseId(100L);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(dto));

        assertEquals("申请类型无效，必须为 PURCHASE 或 RENTAL", ex.getMessage());
    }

    @Test
    void createShouldUseDefaultNameAndPhoneWhenNotProvided() {
        User user = new User();
        user.setId(1L);
        user.setRealName("李四");
        user.setPhone("13900000000");
        when(userMapper.selectById(1L)).thenReturn(user);

        PurchaseApplicationDTO dto = new PurchaseApplicationDTO();
        dto.setType("RENTAL");
        dto.setHouseId(200L);

        service.create(dto);

        ArgumentCaptor<PurchaseApplication> captor = ArgumentCaptor.forClass(PurchaseApplication.class);
        verify(purchaseApplicationMapper).insert(captor.capture());
        assertEquals("RENTAL", captor.getValue().getType());
        assertEquals("李四", captor.getValue().getApplicantName());
        assertEquals("13900000000", captor.getValue().getApplicantPhone());
    }

    // --- approve tests ---

    @Test
    void approveShouldThrowForbiddenWhenNotAdmin() {
        when(userService.isCurrentUserAdmin()).thenReturn(false);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(1L);
        dto.setApproved(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.approve(dto));

        assertEquals("FORBIDDEN", ex.getMessage());
    }

    @Test
    void approveShouldThrowNotFoundWhenApplicationDoesNotExist() {
        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(purchaseApplicationMapper.selectById(999L)).thenReturn(null);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(999L);
        dto.setApproved(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.approve(dto));

        assertEquals("申请不存在", ex.getMessage());
    }

    @Test
    void approveShouldThrowWhenAlreadyProcessed() {
        when(userService.isCurrentUserAdmin()).thenReturn(true);

        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setStatus(1); // already approved
        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(1L);
        dto.setApproved(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.approve(dto));

        assertEquals("该申请已处理，无法重复审批", ex.getMessage());
    }

    @Test
    void approvePurchaseShouldUpdateHouseAndCreateContract() {
        when(userService.isCurrentUserAdmin()).thenReturn(true);

        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setType("PURCHASE");
        app.setHouseId(100L);
        app.setApplicantId(1L);
        app.setApplicantName("张三");
        app.setApplicantPhone("13800000001");
        app.setStatus(0);
        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);

        House house = new House();
        house.setId(100L);
        house.setName("1栋101");
        when(assetMapper.selectById(100L)).thenReturn(house);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(1L);
        dto.setApproved(true);
        dto.setProposedPrice(new BigDecimal("500000"));

        service.approve(dto);

        // Verify house was updated
        ArgumentCaptor<House> houseCaptor = ArgumentCaptor.forClass(House.class);
        verify(assetMapper).updateById(houseCaptor.capture());
        assertEquals(Long.valueOf(1L), houseCaptor.getValue().getOwnerId());
        assertEquals("张三", houseCaptor.getValue().getOwnerName());
        assertEquals("SOLD", houseCaptor.getValue().getStatus());

        // Verify contract was created
        ArgumentCaptor<Contract> contractCaptor = ArgumentCaptor.forClass(Contract.class);
        verify(contractMapper).insert(contractCaptor.capture());
        assertEquals(Long.valueOf(100L), contractCaptor.getValue().getHouseId());
        assertEquals("张三", contractCaptor.getValue().getTenantName());
        assertEquals(new BigDecimal("500000"), contractCaptor.getValue().getRentAmount());

        // Verify application was updated
        ArgumentCaptor<PurchaseApplication> appCaptor = ArgumentCaptor.forClass(PurchaseApplication.class);
        verify(purchaseApplicationMapper).updateById(appCaptor.capture());
        assertEquals(1, appCaptor.getValue().getStatus());
        assertEquals(new BigDecimal("500000"), appCaptor.getValue().getProposedPrice());

        // Verify bills were generated
        verify(financeService, atLeastOnce()).generateBillsSafe(any());
    }

    @Test
    void approvePurchaseShouldRequirePrice() {
        when(userService.isCurrentUserAdmin()).thenReturn(true);

        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setType("PURCHASE");
        app.setHouseId(100L);
        app.setStatus(0);
        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(1L);
        dto.setApproved(true);
        // no price set

        BusinessException ex = assertThrows(BusinessException.class, () -> service.approve(dto));

        assertEquals("审批通过时，出售价格不能为空且必须大于0", ex.getMessage());
    }

    @Test
    void approveRentalShouldCreateRentalContract() {
        when(userService.isCurrentUserAdmin()).thenReturn(true);

        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setType("RENTAL");
        app.setHouseId(100L);
        app.setApplicantId(1L);
        app.setApplicantName("张三");
        app.setApplicantPhone("13800000001");
        app.setStatus(0);
        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);

        House house = new House();
        house.setId(100L);
        house.setName("1栋101");
        when(assetMapper.selectById(100L)).thenReturn(house);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(1L);
        dto.setApproved(true);
        dto.setProposedPrice(new BigDecimal("3000"));
        dto.setStartDate(LocalDate.of(2026, 1, 1));
        dto.setEndDate(LocalDate.of(2026, 12, 31));

        service.approve(dto);

        // Verify house was updated
        ArgumentCaptor<House> houseCaptor = ArgumentCaptor.forClass(House.class);
        verify(assetMapper).updateById(houseCaptor.capture());
        assertEquals("RENTING", houseCaptor.getValue().getStatus());

        // Verify contract was created
        ArgumentCaptor<Contract> contractCaptor = ArgumentCaptor.forClass(Contract.class);
        verify(contractMapper).insert(contractCaptor.capture());
        assertEquals(Long.valueOf(100L), contractCaptor.getValue().getHouseId());
        assertEquals(LocalDate.of(2026, 1, 1), contractCaptor.getValue().getStartDate());
        assertEquals(LocalDate.of(2026, 12, 31), contractCaptor.getValue().getEndDate());
    }

    @Test
    void approveRentalShouldRequirePriceAndDates() {
        when(userService.isCurrentUserAdmin()).thenReturn(true);

        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setType("RENTAL");
        app.setHouseId(100L);
        app.setStatus(0);
        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(1L);
        dto.setApproved(true);
        // no price, no dates

        BusinessException ex = assertThrows(BusinessException.class, () -> service.approve(dto));

        assertEquals("审批通过时，租金、开始日期和结束日期不能为空", ex.getMessage());
    }

    @Test
    void rejectApplicationShouldSetStatusToRejected() {
        when(userService.isCurrentUserAdmin()).thenReturn(true);

        PurchaseApplication app = new PurchaseApplication();
        app.setId(1L);
        app.setType("PURCHASE");
        app.setStatus(0);
        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app);

        PurchaseApprovalDTO dto = new PurchaseApprovalDTO();
        dto.setId(1L);
        dto.setApproved(false);
        dto.setRemark("不符合条件");

        service.approve(dto);

        ArgumentCaptor<PurchaseApplication> captor = ArgumentCaptor.forClass(PurchaseApplication.class);
        verify(purchaseApplicationMapper).updateById(captor.capture());
        assertEquals(2, captor.getValue().getStatus());
    }

    @Test
    void getStatusTextShouldReturnCorrectChineseLabels() {
        // Test via getById which calls getStatusText
        PurchaseApplication app0 = new PurchaseApplication();
        app0.setId(1L);
        app0.setStatus(0);
        app0.setHouseId(100L);
        when(purchaseApplicationMapper.selectById(1L)).thenReturn(app0);
        when(assetMapper.selectById(100L)).thenReturn(null);

        assertEquals("待审批", service.getById(1L).getStatusText());

        // status 1 - 已通过
        PurchaseApplication app1 = new PurchaseApplication();
        app1.setId(2L);
        app1.setStatus(1);
        app1.setHouseId(100L);
        when(purchaseApplicationMapper.selectById(2L)).thenReturn(app1);
        assertEquals("已通过", service.getById(2L).getStatusText());

        // status 2 - 已拒绝
        PurchaseApplication app2 = new PurchaseApplication();
        app2.setId(3L);
        app2.setStatus(2);
        app2.setHouseId(100L);
        when(purchaseApplicationMapper.selectById(3L)).thenReturn(app2);
        assertEquals("已拒绝", service.getById(3L).getStatusText());

        // status 99 - unknown
        PurchaseApplication app99 = new PurchaseApplication();
        app99.setId(4L);
        app99.setStatus(99);
        app99.setHouseId(100L);
        when(purchaseApplicationMapper.selectById(4L)).thenReturn(app99);
        assertEquals("未知", service.getById(4L).getStatusText());
    }
}
