package com.wisdom.service;

import com.wisdom.dto.BillGenerateDTO;
import com.wisdom.dto.BillPageQueryDTO;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.PaymentAuditDTO;
import com.wisdom.dto.PaymentPageQueryDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.BatchRecordVO;
import com.wisdom.vo.BillVO;
import com.wisdom.vo.PaymentVO;

public interface FinanceService {
    BatchRecordVO batchGenerateBills(BillGenerateDTO billGenerateDTO);
    PageResult<BatchRecordVO> getBatchLogs(PageQueryDTO pageQueryDTO);
    PageResult<BillVO> getBillList(BillPageQueryDTO billPageQueryDTO);
    void auditPayment(Long id, PaymentAuditDTO paymentAuditDTO);
    PageResult<PaymentVO> getPaymentList(PaymentPageQueryDTO paymentPageQueryDTO);
}