package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.context.BaseContext;
import com.wisdom.dto.PurchaseApplicationDTO;
import com.wisdom.dto.PurchaseApplicationPageQueryDTO;
import com.wisdom.dto.PurchaseApprovalDTO;
import com.wisdom.entity.Contract;
import com.wisdom.entity.House;
import com.wisdom.entity.PurchaseApplication;
import com.wisdom.entity.User;
import com.wisdom.exception.BusinessException;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.ContractMapper;
import com.wisdom.mapper.PurchaseApplicationMapper;
import com.wisdom.mapper.UserMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.PurchaseApplicationService;
import com.wisdom.service.UserService;
import com.wisdom.vo.PurchaseApplicationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseApplicationServiceImpl implements PurchaseApplicationService {

    @Autowired
    private PurchaseApplicationMapper purchaseApplicationMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public PageResult<PurchaseApplicationVO> getList(PurchaseApplicationPageQueryDTO dto) {
        Page<PurchaseApplication> page = new Page<>(
            dto.getPage() != null ? dto.getPage() : 1,
            dto.getPageSize() != null ? dto.getPageSize() : 10
        );
        LambdaQueryWrapper<PurchaseApplication> queryWrapper = new LambdaQueryWrapper<>();
        if (dto.getStatus() != null) {
            queryWrapper.eq(PurchaseApplication::getStatus, dto.getStatus());
        }
        if (dto.getApplicantName() != null && !dto.getApplicantName().isEmpty()) {
            queryWrapper.like(PurchaseApplication::getApplicantName, dto.getApplicantName());
        }
        if (dto.getHouseId() != null) {
            queryWrapper.eq(PurchaseApplication::getHouseId, dto.getHouseId());
        }
        if (dto.getType() != null && !dto.getType().isEmpty()) {
            queryWrapper.eq(PurchaseApplication::getType, dto.getType());
        }
        if (!userService.isCurrentUserAdmin()) {
            Long currentUserId = userService.getRequiredCurrentUserId();
            queryWrapper.eq(PurchaseApplication::getApplicantId, currentUserId);
        }
        queryWrapper.orderByDesc(PurchaseApplication::getCreateTime);
        IPage<PurchaseApplication> applicationPage = purchaseApplicationMapper.selectPage(page, queryWrapper);
        List<PurchaseApplicationVO> voList = applicationPage.getRecords().stream()
                .map(app -> {
                    PurchaseApplicationVO vo = new PurchaseApplicationVO();
                    BeanUtils.copyProperties(app, vo);
                    House house = assetMapper.selectById(app.getHouseId());
                    if (house != null) {
                        vo.setHouseName(house.getName());
                    }
                    vo.setStatusText(getStatusText(app.getStatus()));
                    return vo;
                })
                .collect(Collectors.toList());
        return new PageResult<>(applicationPage.getTotal(), voList);
    }

    @Override
    public PurchaseApplicationVO getById(Long id) {
        PurchaseApplication app = purchaseApplicationMapper.selectById(id);
        if (app == null) {
            throw BusinessException.notFound("申请不存在");
        }
        PurchaseApplicationVO vo = new PurchaseApplicationVO();
        BeanUtils.copyProperties(app, vo);
        House house = assetMapper.selectById(app.getHouseId());
        if (house != null) {
            vo.setHouseName(house.getName());
        }
        vo.setStatusText(getStatusText(app.getStatus()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PurchaseApplicationDTO dto) {
        Long currentUserId = BaseContext.getCurrentId();
        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw BusinessException.unauthorized();
        }
        String type = dto.getType() != null ? dto.getType() : "PURCHASE";
        if (!"PURCHASE".equals(type) && !"RENTAL".equals(type)) {
            throw BusinessException.badRequest("申请类型无效，必须为 PURCHASE 或 RENTAL");
        }
        PurchaseApplication app = new PurchaseApplication();
        app.setApplicationNo("APP-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        app.setType(type);
        app.setHouseId(dto.getHouseId());
        app.setApplicantId(currentUserId);
        app.setApplicantName(dto.getApplicantName() != null ? dto.getApplicantName() : user.getRealName());
        app.setApplicantPhone(dto.getApplicantPhone() != null ? dto.getApplicantPhone() : user.getPhone());
        app.setStatus(0);
        app.setRemark(dto.getRemark() != null ? dto.getRemark() : "");
        purchaseApplicationMapper.insert(app);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(PurchaseApprovalDTO dto) {
        if (!userService.isCurrentUserAdmin()) {
            throw BusinessException.forbidden();
        }
        PurchaseApplication app = purchaseApplicationMapper.selectById(dto.getId());
        if (app == null) {
            throw BusinessException.notFound("申请不存在");
        }
        if (app.getStatus() != 0) {
            throw BusinessException.badRequest("该申请已处理，无法重复审批");
        }

        String appType = app.getType() != null ? app.getType() : "PURCHASE";

        if (Boolean.TRUE.equals(dto.getApproved())) {
            if ("PURCHASE".equals(appType)) {
                // 购买申请：定价，转移所有权，创建购买合同
                if (dto.getProposedPrice() == null || dto.getProposedPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                    throw BusinessException.badRequest("审批通过时，出售价格不能为空且必须大于0");
                }
                House house = assetMapper.selectById(app.getHouseId());
                if (house != null) {
                    house.setOwnerId(app.getApplicantId());
                    house.setOwnerName(app.getApplicantName());
                    house.setOwnerPhone(app.getApplicantPhone());
                    house.setStatus("SOLD");
                    assetMapper.updateById(house);
                }

                // 创建购买合同记录
                Contract contract = new Contract();
                contract.setHouseId(app.getHouseId());
                contract.setTenantName(app.getApplicantName());
                contract.setRentAmount(dto.getProposedPrice());
                contract.setStartDate(LocalDate.now());
                contract.setContractStatus(1);
                contractMapper.insert(contract);

                app.setStatus(1);
                app.setProposedPrice(dto.getProposedPrice());
                app.setCreatedContractId(contract.getId());
            } else {
                // 租赁申请：需要租金、开始日期、结束日期，创建租赁合同
                if (dto.getProposedPrice() == null || dto.getStartDate() == null || dto.getEndDate() == null) {
                    throw BusinessException.badRequest("审批通过时，租金、开始日期和结束日期不能为空");
                }
                Contract contract = new Contract();
                contract.setHouseId(app.getHouseId());
                contract.setTenantName(app.getApplicantName());
                contract.setRentAmount(dto.getProposedPrice());
                contract.setStartDate(dto.getStartDate());
                contract.setEndDate(dto.getEndDate());
                contract.setDeposit(dto.getDeposit());
                contract.setIncreaseRate(dto.getIncreaseRate());
                contract.setContractStatus(1);
                contractMapper.insert(contract);

                // 更新资产状态为已出租
                House house = assetMapper.selectById(app.getHouseId());
                if (house != null) {
                    house.setOwnerId(app.getApplicantId());
                    house.setOwnerName(app.getApplicantName());
                    house.setOwnerPhone(app.getApplicantPhone());
                    house.setStatus("RENTING");
                    assetMapper.updateById(house);
                }

                app.setStatus(1);
                app.setProposedPrice(dto.getProposedPrice());
                app.setStartDate(dto.getStartDate());
                app.setEndDate(dto.getEndDate());
                app.setCreatedContractId(contract.getId());
            }
            purchaseApplicationMapper.updateById(app);
        } else {
            app.setStatus(2);
            app.setRemark(dto.getRemark() != null ? dto.getRemark() : "");
            purchaseApplicationMapper.updateById(app);
        }
    }

    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审批";
            case 1: return "已通过";
            case 2: return "已拒绝";
            default: return "未知";
        }
    }
}
