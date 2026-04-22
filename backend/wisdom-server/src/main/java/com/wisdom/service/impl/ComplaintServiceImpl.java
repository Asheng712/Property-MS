package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.ComplaintHandleDTO;
import com.wisdom.dto.ComplaintPageQueryDTO;
import com.wisdom.entity.Complaint;
import com.wisdom.mapper.ComplaintMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.ComplaintService;
import com.wisdom.vo.ComplaintVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintMapper complaintMapper;

    @Override
    public PageResult<ComplaintVO> getComplaintList(ComplaintPageQueryDTO complaintPageQueryDTO) {
        Page<Complaint> page = new Page<>(
            complaintPageQueryDTO.getPage() != null ? complaintPageQueryDTO.getPage() : 1,
            complaintPageQueryDTO.getPageSize() != null ? complaintPageQueryDTO.getPageSize() : 10
        );
        LambdaQueryWrapper<Complaint> queryWrapper = new LambdaQueryWrapper<>();
        if (complaintPageQueryDTO.getCategory() != null) {
            queryWrapper.like(Complaint::getCategory, complaintPageQueryDTO.getCategory());
        }
        if (complaintPageQueryDTO.getStatus() != null) {
            queryWrapper.eq(Complaint::getStatus, complaintPageQueryDTO.getStatus());
        }
        IPage<Complaint> complaintPage = complaintMapper.selectPage(page, queryWrapper);
        List<ComplaintVO> complaintVOList = complaintPage.getRecords().stream()
                .map(complaint -> {
                    ComplaintVO complaintVO = new ComplaintVO();
                    BeanUtils.copyProperties(complaint, complaintVO);
                    complaintVO.setStatusText(getStatusText(complaint.getStatus()));
                    return complaintVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(complaintPage.getTotal(), complaintVOList);
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待处理";
            case 1: return "处理中";
            case 2: return "已办结";
            default: return "未知";
        }
    }

    @Override
    public void handleComplaint(ComplaintHandleDTO complaintHandleDTO) {
        Complaint complaint = complaintMapper.selectById(complaintHandleDTO.getId());
        if (complaint == null) {
            throw new RuntimeException("投诉不存在");
        }
        complaint.setStatus(complaintHandleDTO.getStatus());
        complaintMapper.updateById(complaint);
    }
}