package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.ComplaintCreateDTO;
import com.wisdom.dto.ComplaintHandleDTO;
import com.wisdom.dto.ComplaintPageQueryDTO;
import com.wisdom.entity.Complaint;
import com.wisdom.mapper.ComplaintMapper;
import com.wisdom.result.PageResult;
import com.wisdom.context.BaseContext;
import com.wisdom.entity.User;
import com.wisdom.mapper.UserMapper;
import com.wisdom.service.ComplaintService;
import com.wisdom.service.UserService;
import com.wisdom.vo.ComplaintVO;
import com.wisdom.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

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
        if (!userService.isCurrentUserAdmin()) {
            Long currentUserId = userService.getRequiredCurrentUserId();
            queryWrapper.eq(Complaint::getReporterId, currentUserId);
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
            throw BusinessException.notFound("投诉不存在");
        }
        complaint.setStatus(complaintHandleDTO.getStatus());
        complaint.setHandleResult(complaintHandleDTO.getHandleResult());
        complaintMapper.updateById(complaint);
    }

    @Override
    public void createComplaint(ComplaintCreateDTO complaintCreateDTO) {
        Long currentUserId = BaseContext.getCurrentId();
        User user = userMapper.selectById(currentUserId);
        // 来源优先使用前端传入的真实姓名；若前端传入的是账号名或无意义值，则用数据库中的真实姓名
        String source = complaintCreateDTO.getSource();
        if (source == null || source.isEmpty() || "APP".equals(source) || "前台代录".equals(source)) {
            // 留空或默认值，从用户信息中获取真实姓名
            source = (user != null && user.getRealName() != null) ? user.getRealName() : source;
        }
        Complaint complaint = new Complaint();
        complaint.setComplaintNo("CMP-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        complaint.setCategory(complaintCreateDTO.getCategory());
        complaint.setContent(complaintCreateDTO.getContent());
        complaint.setSource(source);
        complaint.setReporterId(currentUserId);
        complaint.setStatus(0);
        complaint.setCreateTime(LocalDateTime.now());
        complaintMapper.insert(complaint);
    }
}