package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.ContractDTO;
import com.wisdom.dto.ContractPageQueryDTO;
import com.wisdom.entity.Contract;
import com.wisdom.entity.House;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.ContractMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.FinanceService;
import com.wisdom.service.UserService;
import com.wisdom.vo.ContractVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock
    private ContractMapper contractMapper;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private UserService userService;

    @Mock
    private FinanceService financeService;

    @InjectMocks
    private ContractServiceImpl contractService;

    @Test
    void getContractListShouldReturnMappedPageWhenAdmin() {
        ContractPageQueryDTO dto = new ContractPageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(10);
        dto.setTenantName("张");
        dto.setContractStatus(1);

        Contract contract = new Contract();
        contract.setId(1L);
        contract.setHouseId(100L);
        contract.setTenantName("张三");
        contract.setRentAmount(new BigDecimal("2000"));
        contract.setContractStatus(1);

        Page<Contract> page = new Page<>(1, 10);
        page.setRecords(List.of(contract));
        page.setTotal(1);

        House house = new House();
        house.setId(100L);
        house.setName("1栋101");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(contractMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(assetMapper.selectById(100L)).thenReturn(house);

        PageResult<ContractVO> result = contractService.getContractList(dto);

        assertEquals(1, result.getTotal());
        ContractVO vo = result.getRecords().get(0);
        assertEquals("张三", vo.getTenantName());
        assertEquals("1栋101", vo.getHouseName());
        assertEquals("有效", vo.getContractStatusText());
    }

    @Test
    void getContractListShouldFilterByOwnedHousesWhenNonAdmin() {
        ContractPageQueryDTO dto = new ContractPageQueryDTO();

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(2L);
        when(userService.getOwnedHouseIds(2L)).thenReturn(List.of(100L, 101L));

        Page<Contract> page = new Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);

        when(contractMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        contractService.getContractList(dto);

        verify(contractMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void getContractListShouldReturnEmptyWhenNonAdminHasNoHouses() {
        ContractPageQueryDTO dto = new ContractPageQueryDTO();

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(3L);
        when(userService.getOwnedHouseIds(3L)).thenReturn(Collections.emptyList());

        PageResult<ContractVO> result = contractService.getContractList(dto);

        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());
    }

    @Test
    void getContractListShouldSetExpiredText() {
        ContractPageQueryDTO dto = new ContractPageQueryDTO();
        Contract contract = new Contract();
        contract.setId(2L);
        contract.setHouseId(200L);
        contract.setContractStatus(0);

        Page<Contract> page = new Page<>(1, 10);
        page.setRecords(List.of(contract));
        page.setTotal(1);

        House house = new House();
        house.setId(200L);
        house.setName("2栋202");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(contractMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(assetMapper.selectById(200L)).thenReturn(house);

        PageResult<ContractVO> result = contractService.getContractList(dto);

        assertEquals("过期/终止", result.getRecords().get(0).getContractStatusText());
    }

    @Test
    void saveOrUpdateContractShouldInsertWhenIdIsNull() {
        ContractDTO dto = new ContractDTO();
        dto.setTenantName("新租户");
        dto.setRentAmount(new BigDecimal("3000"));

        contractService.saveOrUpdateContract(dto);

        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        verify(contractMapper, times(1)).insert(captor.capture());
        verify(contractMapper, never()).updateById(any(Contract.class));
        assertEquals("新租户", captor.getValue().getTenantName());
    }

    @Test
    void saveOrUpdateContractShouldUpdateWhenIdExists() {
        ContractDTO dto = new ContractDTO();
        dto.setId(5L);
        dto.setTenantName("更新租户");

        contractService.saveOrUpdateContract(dto);

        verify(contractMapper, never()).insert(any(Contract.class));
        verify(contractMapper, times(1)).updateById(any(Contract.class));
    }
}
