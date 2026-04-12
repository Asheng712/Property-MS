package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.ContractDTO;
import com.wisdom.dto.ContractPageQueryDTO;
import com.wisdom.entity.Contract;
import com.wisdom.entity.House;
import com.wisdom.mapper.ContractMapper;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.ContractService;
import com.wisdom.vo.ContractVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Override
    public PageResult<ContractVO> getContractList(ContractPageQueryDTO contractPageQueryDTO) {
        Page<Contract> page = new Page<>(contractPageQueryDTO.getPage(), contractPageQueryDTO.getPageSize());
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        if (contractPageQueryDTO.getTenantName() != null) {
            queryWrapper.like(Contract::getTenantName, contractPageQueryDTO.getTenantName());
        }
        if (contractPageQueryDTO.getContractStatus() != null) {
            queryWrapper.eq(Contract::getContractStatus, contractPageQueryDTO.getContractStatus());
        }
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
    public void saveOrUpdateContract(ContractDTO contractDTO) {
        Contract contract = new Contract();
        BeanUtils.copyProperties(contractDTO, contract);
        if (contractDTO.getId() == null) {
            contractMapper.insert(contract);
        } else {
            contractMapper.updateById(contract);
        }
    }
}