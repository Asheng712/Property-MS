package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.BillGenerateDTO;
import com.wisdom.dto.ContractDTO;
import com.wisdom.dto.ContractPageQueryDTO;
import com.wisdom.entity.Contract;
import com.wisdom.entity.House;
import com.wisdom.enumeration.FeeTypeEnum;
import com.wisdom.mapper.ContractMapper;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.ContractService;
import com.wisdom.service.FinanceService;
import com.wisdom.service.UserService;
import com.wisdom.vo.ContractVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private FinanceService financeService;

    @Override
    public PageResult<ContractVO> getContractList(ContractPageQueryDTO contractPageQueryDTO) {
        Page<Contract> page = new Page<>(
            contractPageQueryDTO.getPage() != null ? contractPageQueryDTO.getPage() : 1,
            contractPageQueryDTO.getPageSize() != null ? contractPageQueryDTO.getPageSize() : 10
        );
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        if (contractPageQueryDTO.getTenantName() != null) {
            queryWrapper.like(Contract::getTenantName, contractPageQueryDTO.getTenantName());
        }
        if (contractPageQueryDTO.getContractStatus() != null) {
            queryWrapper.eq(Contract::getContractStatus, contractPageQueryDTO.getContractStatus());
        }
        if (!userService.isCurrentUserAdmin()) {
            Long currentUserId = userService.getRequiredCurrentUserId();
            List<Long> ownedHouseIds = userService.getOwnedHouseIds(currentUserId);
            if (ownedHouseIds.isEmpty()) {
                return new PageResult<>(0, java.util.Collections.emptyList());
            }
            queryWrapper.in(Contract::getHouseId, ownedHouseIds);
        }
        queryWrapper.orderByDesc(Contract::getCreateTime);
        IPage<Contract> contractPage = contractMapper.selectPage(page, queryWrapper);
        List<ContractVO> contractVOList = contractPage.getRecords().stream()
                .map(contract -> {
                    ContractVO contractVO = new ContractVO();
                    BeanUtils.copyProperties(contract, contractVO);
                    House house = assetMapper.selectById(contract.getHouseId());
                    if (house != null) {
                        contractVO.setHouseName(house.getName());
                    }
                    contractVO.setContractStatusText(contract.getContractStatus() == 1 ? "有效" : "过期/终止");
                    return contractVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(contractPage.getTotal(), contractVOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateContract(ContractDTO contractDTO) {
        Contract contract = new Contract();
        BeanUtils.copyProperties(contractDTO, contract);
        if (contractDTO.getId() == null) {
            // 新增合同
            contractMapper.insert(contract);
            House house = assetMapper.selectById(contract.getHouseId());
            if (house != null && contract.getContractStatus() != null && contract.getContractStatus() == 1) {
                house.setStatus("RENTING");
                house.setOwnerName(contract.getTenantName());
                assetMapper.updateById(house);

                // 自动生成账单
                String startMonth = contract.getStartDate() != null
                    ? contract.getStartDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))
                    : java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                boolean isRental = contract.getDeposit() != null
                    && contract.getDeposit().compareTo(java.math.BigDecimal.ZERO) > 0;

                try {
                    BillGenerateDTO primaryDTO = new BillGenerateDTO();
                    primaryDTO.setFeeType(isRental ? FeeTypeEnum.RENT.getCode() : FeeTypeEnum.PURCHASE.getCode());
                    primaryDTO.setBillMonth(startMonth);
                    primaryDTO.setHouseId(contract.getHouseId());
                    financeService.generateBillsSafe(primaryDTO);
                } catch (Exception e) { /* 不阻塞 */ }
                if (isRental) {
                    try {
                        BillGenerateDTO depositDTO = new BillGenerateDTO();
                        depositDTO.setFeeType(FeeTypeEnum.DEPOSIT.getCode());
                        depositDTO.setBillMonth(startMonth);
                        depositDTO.setHouseId(contract.getHouseId());
                        financeService.generateBillsSafe(depositDTO);
                    } catch (Exception e) { /* 不阻塞 */ }
                }
                try {
                    BillGenerateDTO propertyDTO = new BillGenerateDTO();
                    propertyDTO.setFeeType(FeeTypeEnum.PROPERTY.getCode());
                    propertyDTO.setBillMonth(startMonth);
                    propertyDTO.setHouseId(contract.getHouseId());
                    financeService.generateBillsSafe(propertyDTO);
                } catch (Exception e) { /* 不阻塞 */ }
            }
        } else {
            // 编辑合同：若合同终止则释放资产为空置
            contractMapper.updateById(contract);
            if (contract.getContractStatus() != null && contract.getContractStatus() == 0) {
                House house = assetMapper.selectById(contract.getHouseId());
                if (house != null) {
                    house.setStatus("VACANT");
                    house.setOwnerName(null);
                    house.setOwnerPhone(null);
                    house.setOwnerId(null);
                    assetMapper.updateById(house);
                }
            }
        }
    }
}