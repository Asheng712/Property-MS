package com.wisdom.service;

import com.wisdom.dto.ComplaintHandleDTO;
import com.wisdom.dto.ComplaintPageQueryDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.ComplaintVO;

public interface ComplaintService {
    PageResult<ComplaintVO> getComplaintList(ComplaintPageQueryDTO complaintPageQueryDTO);
    void handleComplaint(ComplaintHandleDTO complaintHandleDTO);
}