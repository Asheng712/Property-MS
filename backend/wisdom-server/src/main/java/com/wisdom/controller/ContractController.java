package com.wisdom.controller;

import com.wisdom.dto.ContractDTO;
import com.wisdom.dto.ContractPageQueryDTO;
import com.wisdom.result.Result;
import com.wisdom.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contracts")
@Tag(name = "合同管理模块", description = "合同列表查询、合同管理等接口")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping
    @Operation(summary = "分页查询合同")
    public Result<?> getContractList(ContractPageQueryDTO contractPageQueryDTO) {
        return Result.success(contractService.getContractList(contractPageQueryDTO));
    }

    @PostMapping
    @Operation(summary = "新增合同")
    public Result<Void> addContract(@RequestBody ContractDTO contractDTO) {
        contractService.saveOrUpdateContract(contractDTO);
        return Result.success(null);
    }

    @PutMapping
    @Operation(summary = "终止合同")
    public Result<Void> updateContract(@RequestBody ContractDTO contractDTO) {
        contractService.saveOrUpdateContract(contractDTO);
        return Result.success(null);
    }
}