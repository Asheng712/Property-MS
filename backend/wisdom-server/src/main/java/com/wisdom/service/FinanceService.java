package com.wisdom.service;

import com.wisdom.dto.*;
import com.wisdom.result.PageResult;
import com.wisdom.vo.BillVO;
import com.wisdom.vo.PaymentRecordVO;
import com.wisdom.vo.PropertyFeeConfigVO;

public interface FinanceService {
    // 账单
    void generateBills(BillGenerateDTO dto);
    void generateBillsSafe(BillGenerateDTO dto);
    PageResult<BillVO> getBillList(BillPageQueryDTO dto);

    // 支付记录
    PaymentRecordVO submitPayment(PaymentSubmitDTO dto);
    void verifyPayment(Long id);
    void cancelPayment(PaymentCancelDTO dto);
    PageResult<PaymentRecordVO> getPaymentList(PaymentPageQueryDTO dto);
    PaymentRecordVO getPaymentDetail(Long id);

    // 物业费配置
    void setPropertyFeeConfig(PropertyFeeConfigDTO dto);
    PropertyFeeConfigVO getCurrentPropertyFeeConfig();
}
