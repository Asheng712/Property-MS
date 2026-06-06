package com.wisdom.controller;

import com.wisdom.annotation.LoginRequired;
import com.wisdom.dto.PurchaseApplicationDTO;
import com.wisdom.dto.PurchaseApplicationPageQueryDTO;
import com.wisdom.dto.PurchaseApprovalDTO;
import com.wisdom.result.Result;
import com.wisdom.service.PurchaseApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-applications")
@Tag(name = "资产购买申请模块", description = "资产购买申请提交、查询、审批等接口")
public class PurchaseApplicationController {

    @Autowired
    private PurchaseApplicationService purchaseApplicationService;

    @GetMapping
    @Operation(summary = "分页查询购买申请")
    @LoginRequired
    public Result<?> getList(PurchaseApplicationPageQueryDTO dto) {
        return Result.success(purchaseApplicationService.getList(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询购买申请详情")
    @LoginRequired
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(purchaseApplicationService.getById(id));
    }

    @PostMapping
    @Operation(summary = "提交购买申请")
    @LoginRequired
    public Result<Void> create(@RequestBody PurchaseApplicationDTO dto) {
        purchaseApplicationService.create(dto);
        return Result.success(null);
    }

    @PutMapping("/approve")
    @Operation(summary = "审批购买申请（通过后自动生成合同）")
    public Result<Void> approve(@RequestBody PurchaseApprovalDTO dto) {
        purchaseApplicationService.approve(dto);
        return Result.success(null);
    }
}
