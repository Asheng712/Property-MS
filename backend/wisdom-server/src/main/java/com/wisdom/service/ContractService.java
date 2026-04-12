package com.wisdom.service;

import com.wisdom.dto.ContractDTO;
import com.wisdom.dto.ContractPageQueryDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.ContractVO;

public interface ContractService {
    PageResult<ContractVO> getContractList(ContractPageQueryDTO contractPageQueryDTO);
    void saveOrUpdateContract(ContractDTO contractDTO);
}