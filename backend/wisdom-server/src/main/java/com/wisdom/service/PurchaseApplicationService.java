package com.wisdom.service;

import com.wisdom.dto.PurchaseApplicationDTO;
import com.wisdom.dto.PurchaseApplicationPageQueryDTO;
import com.wisdom.dto.PurchaseApprovalDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.PurchaseApplicationVO;

public interface PurchaseApplicationService {
    PageResult<PurchaseApplicationVO> getList(PurchaseApplicationPageQueryDTO dto);
    PurchaseApplicationVO getById(Long id);
    void create(PurchaseApplicationDTO dto);
    void approve(PurchaseApprovalDTO dto);
}
