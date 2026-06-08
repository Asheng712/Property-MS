package com.wisdom.controller;

import com.wisdom.annotation.LoginRequired;
import com.wisdom.dto.*;
import com.wisdom.result.Result;
import com.wisdom.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "财务与计费模块", description = "账单生成、在线缴费、核销等接口")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    // ======================== 账单 ========================

    @PostMapping("/bills/generate")
    @Operation(summary = "生成账单（根据合同和费用类型）")
    @LoginRequired
    public Result<Void> generateBills(@RequestBody BillGenerateDTO dto) {
        financeService.generateBills(dto);
        return Result.success(null);
    }

    @GetMapping("/bills")
    @Operation(summary = "分页查询账单")
    @LoginRequired
    public Result<?> getBillList(BillPageQueryDTO dto) {
        return Result.success(financeService.getBillList(dto));
    }

    // ======================== 支付记录 ========================

    @PostMapping("/payments")
    @Operation(summary = "用户提交缴费（支持多账单合并）")
    @LoginRequired
    public Result<?> submitPayment(@Valid @RequestBody PaymentSubmitDTO dto) {
        return Result.success(financeService.submitPayment(dto));
    }

    @GetMapping("/payments")
    @Operation(summary = "分页查询支付记录")
    @LoginRequired
    public Result<?> getPaymentList(PaymentPageQueryDTO dto) {
        return Result.success(financeService.getPaymentList(dto));
    }

    @GetMapping("/payments/{id}")
    @Operation(summary = "支付记录详情（含账单明细）")
    @LoginRequired
    public Result<?> getPaymentDetail(@PathVariable Long id) {
        return Result.success(financeService.getPaymentDetail(id));
    }

    @PutMapping("/payments/{id}/verify")
    @Operation(summary = "管理员核销支付记录")
    @LoginRequired
    public Result<Void> verifyPayment(@PathVariable Long id) {
        financeService.verifyPayment(id);
        return Result.success(null);
    }

    @PutMapping("/payments/{id}/cancel")
    @Operation(summary = "管理员撤销已核销支付记录")
    @LoginRequired
    public Result<Void> cancelPayment(@PathVariable Long id, @Valid @RequestBody PaymentCancelDTO dto) {
        dto.setId(id);
        financeService.cancelPayment(dto);
        return Result.success(null);
    }

    // ======================== 物业费配置 ========================

    @PostMapping("/property-fee-config")
    @Operation(summary = "设置物业费单价（下月生效）")
    @LoginRequired
    public Result<Void> setPropertyFeeConfig(@Valid @RequestBody PropertyFeeConfigDTO dto) {
        financeService.setPropertyFeeConfig(dto);
        return Result.success(null);
    }

    @GetMapping("/property-fee-config")
    @Operation(summary = "查询当前生效的物业费配置")
    @LoginRequired
    public Result<?> getCurrentPropertyFeeConfig() {
        return Result.success(financeService.getCurrentPropertyFeeConfig());
    }
}
