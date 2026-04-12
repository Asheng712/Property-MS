package com.wisdom.controller;

import com.wisdom.dto.BillGenerateDTO;
import com.wisdom.dto.BillPageQueryDTO;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.PaymentAuditDTO;
import com.wisdom.dto.PaymentPageQueryDTO;
import com.wisdom.result.Result;
import com.wisdom.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "财务与计费模块", description = "账单生成、财务流水核销等接口")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @PostMapping("/bills/batch-generate")
    @Operation(summary = "批量生成月度账单")
    public Result<?> batchGenerateBills(@RequestBody BillGenerateDTO billGenerateDTO) {
        return Result.success(financeService.batchGenerateBills(billGenerateDTO));
    }

    @GetMapping("/bills/batch-logs")
    @Operation(summary = "查询账单批处理历史")
    public Result<?> getBatchLogs(PageQueryDTO pageQueryDTO) {
        return Result.success(financeService.getBatchLogs(pageQueryDTO));
    }

    @GetMapping("/bills")
    @Operation(summary = "分页查询账单明细")
    public Result<?> getBillList(BillPageQueryDTO billPageQueryDTO) {
        return Result.success(financeService.getBillList(billPageQueryDTO));
    }

    @PutMapping("/finance/audit/{id}")
    @Operation(summary = "财务流水核销确认")
    public Result<Void> auditPayment(@PathVariable Long id, @RequestBody PaymentAuditDTO paymentAuditDTO) {
        financeService.auditPayment(id, paymentAuditDTO);
        return Result.success(null);
    }

    @GetMapping("/finance/payments")
    @Operation(summary = "分页查询缴费流水")
    public Result<?> getPaymentList(PaymentPageQueryDTO paymentPageQueryDTO) {
        return Result.success(financeService.getPaymentList(paymentPageQueryDTO));
    }
}