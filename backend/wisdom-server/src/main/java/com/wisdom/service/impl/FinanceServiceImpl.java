package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.context.BaseContext;
import com.wisdom.dto.*;
import com.wisdom.entity.*;
import com.wisdom.enumeration.*;
import com.wisdom.exception.BusinessException;
import com.wisdom.mapper.*;
import com.wisdom.result.PageResult;
import com.wisdom.service.FinanceService;
import com.wisdom.service.UserService;
import com.wisdom.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired private BillMapper billMapper;
    @Autowired private PaymentRecordMapper paymentRecordMapper;
    @Autowired private PaymentRecordBillMapper paymentRecordBillMapper;
    @Autowired private PropertyFeeConfigMapper propertyFeeConfigMapper;
    @Autowired private AssetMapper assetMapper;
    @Autowired private ContractMapper contractMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private UserService userService;

    @Autowired private BillBatchMapper billBatchMapper;

    // ======================== 账单生成 ========================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateBills(BillGenerateDTO dto) {
        doGenerateBills(dto);
    }

    @Override
    public void generateBillsSafe(BillGenerateDTO dto) {
        try {
            doGenerateBills(dto);
        } catch (Exception e) {
            log.error("自动生成账单失败: feeType={}, billMonth={}, houseId={}", dto.getFeeType(), dto.getBillMonth(), dto.getHouseId(), e);
        }
    }

    private void doGenerateBills(BillGenerateDTO dto) {
        Integer feeType = dto.getFeeType();
        String billMonth = dto.getBillMonth() != null
            ? dto.getBillMonth()
            : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 物业费使用下月作为计费月
        if (FeeTypeEnum.PROPERTY.getCode().equals(feeType)) {
            billMonth = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        // 获取物业费单价（月账单月份）
        PropertyFeeConfig config = null;
        if (FeeTypeEnum.PROPERTY.getCode().equals(feeType)) {
            config = propertyFeeConfigMapper.getEffectiveConfig(billMonth);
            if (config == null) {
                throw BusinessException.badRequest("未找到" + billMonth + "的物业费配置，请先设置单价");
            }
        }

        List<House> houses;
        if (dto.getHouseId() != null) {
            House house = assetMapper.selectById(dto.getHouseId());
            if (house == null) throw BusinessException.notFound("房屋不存在");
            houses = List.of(house);
        } else {
            houses = assetMapper.selectAllAssets();
        }

        int count = 0;
        for (House house : houses) {
            // 跳过楼栋(BUILDING)和空置房产
            if ("BUILDING".equals(house.getType())) continue;
            if (house.getOwnerId() == null) continue;

            // 检查是否重复
            LambdaQueryWrapper<Bill> dupQuery = new LambdaQueryWrapper<>();
            dupQuery.eq(Bill::getHouseId, house.getId())
                    .eq(Bill::getFeeType, feeType)
                    .eq(Bill::getBillMonth, billMonth)
                    .ne(Bill::getStatus, BillStatusEnum.VOIDED.getCode());
            if (billMapper.selectCount(dupQuery) > 0) continue;

            List<Contract> contracts = contractMapper.selectByHouseId(house.getId());
            if (contracts.isEmpty() && !FeeTypeEnum.PROPERTY.getCode().equals(feeType)) continue;

            Bill bill = new Bill();
            bill.setUserId(house.getOwnerId());
            bill.setHouseId(house.getId());
            bill.setFeeType(feeType);
            bill.setBillNo("BILL-" + feeType + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "-" + count);
            bill.setBillMonth(billMonth);
            bill.setStatus(BillStatusEnum.PENDING.getCode());
            bill.setDueDate(YearMonth.parse(billMonth).atEndOfMonth());
            bill.setRemark(dto.getRemark() != null ? dto.getRemark() : "");
            bill.setCreateTime(LocalDateTime.now());

            // 根据费用类型计算金额
            BigDecimal amount = calculateBillAmount(feeType, house, contracts, config);
            bill.setAmount(amount);

            if (!contracts.isEmpty()) {
                bill.setContractId(contracts.get(0).getId());
            }

            billMapper.insert(bill);
            count++;
        }

        if (count == 0) {
            throw BusinessException.badRequest("没有符合条件的房屋可生成账单（可能已有同期账单或无可计费房屋）");
        }
    }

    private BigDecimal calculateBillAmount(Integer feeType, House house, List<Contract> contracts, PropertyFeeConfig config) {
        FeeTypeEnum type = FeeTypeEnum.fromCode(feeType);
        if (type == null) throw BusinessException.badRequest("未知费用类型");

        switch (type) {
            case RENT:
                if (contracts.isEmpty()) return BigDecimal.ZERO;
                return contracts.get(0).getRentAmount() != null ? contracts.get(0).getRentAmount() : BigDecimal.ZERO;
            case PURCHASE:
                if (contracts.isEmpty()) return BigDecimal.ZERO;
                return contracts.get(0).getRentAmount() != null ? contracts.get(0).getRentAmount() : BigDecimal.ZERO;
            case DEPOSIT:
                if (contracts.isEmpty()) return BigDecimal.ZERO;
                return contracts.get(0).getDeposit() != null ? contracts.get(0).getDeposit() : BigDecimal.ZERO;
            case PROPERTY:
                if (config == null) return BigDecimal.ZERO;
                BigDecimal area = house.getArea() != null ? house.getArea() : BigDecimal.ZERO;
                return area.multiply(config.getUnitPrice()).setScale(2, java.math.RoundingMode.HALF_UP);
            default:
                return BigDecimal.ZERO;
        }
    }

    // ======================== 账单查询 ========================

    @Override
    public PageResult<BillVO> getBillList(BillPageQueryDTO dto) {
        Page<Bill> page = new Page<>(
            dto.getPage() != null ? dto.getPage() : 1,
            dto.getPageSize() != null ? dto.getPageSize() : 10
        );
        LambdaQueryWrapper<Bill> qw = new LambdaQueryWrapper<>();
        if (dto.getBillNo() != null) qw.like(Bill::getBillNo, dto.getBillNo());
        if (dto.getHouseId() != null) qw.eq(Bill::getHouseId, dto.getHouseId());
        if (dto.getFeeType() != null) qw.eq(Bill::getFeeType, dto.getFeeType());
        if (dto.getStatus() != null) qw.eq(Bill::getStatus, dto.getStatus());
        if (dto.getBillMonth() != null) qw.eq(Bill::getBillMonth, dto.getBillMonth());
        if (!userService.isCurrentUserAdmin()) {
            Long uid = userService.getRequiredCurrentUserId();
            qw.eq(Bill::getUserId, uid);
        }
        qw.orderByDesc(Bill::getCreateTime);

        IPage<Bill> result = billMapper.selectPage(page, qw);
        List<BillVO> voList = result.getRecords().stream().map(this::toBillVO).collect(Collectors.toList());
        return new PageResult<>(result.getTotal(), voList);
    }

    private BillVO toBillVO(Bill bill) {
        BillVO vo = new BillVO();
        vo.setId(bill.getId());
        vo.setUserId(bill.getUserId());
        vo.setHouseId(bill.getHouseId());
        vo.setContractId(bill.getContractId());
        vo.setFeeType(bill.getFeeType());
        vo.setFeeTypeText(FeeTypeEnum.fromCode(bill.getFeeType()) != null
            ? FeeTypeEnum.fromCode(bill.getFeeType()).getLabel() : "未知");
        vo.setBillNo(bill.getBillNo());
        vo.setBillMonth(bill.getBillMonth());
        vo.setAmount(bill.getAmount());
        vo.setStatus(bill.getStatus());
        vo.setStatusText(BillStatusEnum.fromCode(bill.getStatus()) != null
            ? BillStatusEnum.fromCode(bill.getStatus()).getLabel() : "未知");
        vo.setDueDate(bill.getDueDate());
        vo.setRemark(bill.getRemark());
        vo.setCreateTime(bill.getCreateTime());

        House house = assetMapper.selectById(bill.getHouseId());
        if (house != null) vo.setHouseName(house.getName());
        return vo;
    }

    // ======================== 用户提交缴费 ========================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecordVO submitPayment(PaymentSubmitDTO dto) {
        Long uid = userService.getRequiredCurrentUserId();
        List<Bill> bills = billMapper.selectBatchIds(dto.getBillIds());

        if (bills.isEmpty()) throw BusinessException.badRequest("未找到账单");
        if (bills.size() != dto.getBillIds().size()) throw BusinessException.badRequest("部分账单不存在");

        BigDecimal totalExpected = BigDecimal.ZERO;
        for (Bill bill : bills) {
            if (!bill.getUserId().equals(uid)) throw BusinessException.badRequest("账单不属于当前用户");
            if (BillStatusEnum.PENDING.getCode().equals(bill.getStatus())) continue;
            throw BusinessException.badRequest("账单 " + bill.getBillNo() + " 状态不正确（当前: " + BillStatusEnum.fromCode(bill.getStatus()).getLabel() + "）");
            // 只允许待缴费状态的
        }
        // 实际上上面的循环逻辑有点问题，让我修正：
        // 重新过滤：只允许待缴费状态的账单
        for (Bill bill : bills) {
            if (!bill.getUserId().equals(uid)) throw BusinessException.badRequest("账单不属于当前用户");
            if (!BillStatusEnum.PENDING.getCode().equals(bill.getStatus())) {
                throw BusinessException.badRequest("账单 " + bill.getBillNo() + " 状态不正确（当前: "
                    + (BillStatusEnum.fromCode(bill.getStatus()) != null
                        ? BillStatusEnum.fromCode(bill.getStatus()).getLabel() : "未知") + "）");
            }
            totalExpected = totalExpected.add(bill.getAmount());
        }

        // 创建支付记录
        PaymentRecord pr = new PaymentRecord();
        pr.setPaymentNo("PAY-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        pr.setUserId(uid);
        pr.setAmount(totalExpected);
        pr.setPayMethod(dto.getPayMethod());
        pr.setPayTime(LocalDateTime.now());
        pr.setStatus(PaymentStatusEnum.PENDING.getCode());
        pr.setVoucherUrl(dto.getVoucherUrl() != null ? dto.getVoucherUrl() : "");
        pr.setRemark(dto.getRemark() != null ? dto.getRemark() : "");
        pr.setCreateTime(LocalDateTime.now());
        paymentRecordMapper.insert(pr);

        // 创建支付明细并更新账单状态
        for (Bill bill : bills) {
            PaymentRecordBill prb = new PaymentRecordBill();
            prb.setPaymentRecordId(pr.getId());
            prb.setBillId(bill.getId());
            prb.setAmount(bill.getAmount());
            prb.setCreateTime(LocalDateTime.now());
            paymentRecordBillMapper.insert(prb);

            bill.setStatus(BillStatusEnum.SUBMITTED.getCode());
            billMapper.updateById(bill);
        }

        return toPaymentRecordVO(pr);
    }

    // ======================== 管理员核销 ========================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyPayment(Long id) {
        PaymentRecord pr = paymentRecordMapper.selectById(id);
        if (pr == null) throw BusinessException.notFound("支付记录不存在");
        if (!PaymentStatusEnum.PENDING.getCode().equals(pr.getStatus())) {
            throw BusinessException.badRequest("仅待核销状态的记录可以核销");
        }

        Long adminId = BaseContext.getCurrentId();

        pr.setStatus(PaymentStatusEnum.VERIFIED.getCode());
        pr.setVerifyUser(adminId);
        pr.setVerifyTime(LocalDateTime.now());
        paymentRecordMapper.updateById(pr);

        // 更新关联账单
        LambdaQueryWrapper<PaymentRecordBill> prbQw = new LambdaQueryWrapper<>();
        prbQw.eq(PaymentRecordBill::getPaymentRecordId, id);
        List<PaymentRecordBill> details = paymentRecordBillMapper.selectList(prbQw);
        for (PaymentRecordBill d : details) {
            Bill bill = billMapper.selectById(d.getBillId());
            if (bill != null) {
                bill.setStatus(BillStatusEnum.PAID.getCode());
                billMapper.updateById(bill);
            }
        }
    }

    // ======================== 管理员撤销核销 ========================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPayment(PaymentCancelDTO dto) {
        PaymentRecord pr = paymentRecordMapper.selectById(dto.getId());
        if (pr == null) throw BusinessException.notFound("支付记录不存在");
        if (!PaymentStatusEnum.VERIFIED.getCode().equals(pr.getStatus())) {
            throw BusinessException.badRequest("仅已核销状态的记录可以撤销");
        }

        Long adminId = BaseContext.getCurrentId();

        pr.setStatus(PaymentStatusEnum.CANCELLED.getCode());
        pr.setCancelUser(adminId);
        pr.setCancelTime(LocalDateTime.now());
        pr.setCancelReason(dto.getCancelReason());
        paymentRecordMapper.updateById(pr);

        // 关联账单回退为待缴费
        LambdaQueryWrapper<PaymentRecordBill> prbQw = new LambdaQueryWrapper<>();
        prbQw.eq(PaymentRecordBill::getPaymentRecordId, dto.getId());
        List<PaymentRecordBill> details = paymentRecordBillMapper.selectList(prbQw);
        for (PaymentRecordBill d : details) {
            Bill bill = billMapper.selectById(d.getBillId());
            if (bill != null && BillStatusEnum.PAID.getCode().equals(bill.getStatus())) {
                bill.setStatus(BillStatusEnum.PENDING.getCode());
                billMapper.updateById(bill);
            }
        }
    }

    // ======================== 支付记录查询 ========================

    @Override
    public PageResult<PaymentRecordVO> getPaymentList(PaymentPageQueryDTO dto) {
        Page<PaymentRecord> page = new Page<>(
            dto.getPage() != null ? dto.getPage() : 1,
            dto.getPageSize() != null ? dto.getPageSize() : 10
        );
        LambdaQueryWrapper<PaymentRecord> qw = new LambdaQueryWrapper<>();
        if (dto.getPaymentNo() != null) qw.like(PaymentRecord::getPaymentNo, dto.getPaymentNo());
        if (dto.getStatus() != null) qw.eq(PaymentRecord::getStatus, dto.getStatus());
        if (dto.getPayMethod() != null) qw.eq(PaymentRecord::getPayMethod, dto.getPayMethod());
        if (!userService.isCurrentUserAdmin()) {
            Long uid = userService.getRequiredCurrentUserId();
            qw.eq(PaymentRecord::getUserId, uid);
        }
        qw.orderByDesc(PaymentRecord::getCreateTime);

        IPage<PaymentRecord> result = paymentRecordMapper.selectPage(page, qw);
        List<PaymentRecordVO> voList = result.getRecords().stream().map(this::toPaymentRecordVO).collect(Collectors.toList());
        return new PageResult<>(result.getTotal(), voList);
    }

    @Override
    public PaymentRecordVO getPaymentDetail(Long id) {
        PaymentRecord pr = paymentRecordMapper.selectById(id);
        if (pr == null) throw BusinessException.notFound("支付记录不存在");
        return toPaymentRecordVO(pr);
    }

    private PaymentRecordVO toPaymentRecordVO(PaymentRecord pr) {
        PaymentRecordVO vo = new PaymentRecordVO();
        vo.setId(pr.getId());
        vo.setPaymentNo(pr.getPaymentNo());
        vo.setUserId(pr.getUserId());
        vo.setAmount(pr.getAmount());
        vo.setPayMethod(pr.getPayMethod());
        vo.setPayMethodText(PayMethodEnum.fromCode(pr.getPayMethod()) != null
            ? PayMethodEnum.fromCode(pr.getPayMethod()).getLabel() : "未知");
        vo.setPayTime(pr.getPayTime());
        vo.setStatus(pr.getStatus());
        vo.setStatusText(PaymentStatusEnum.fromCode(pr.getStatus()) != null
            ? PaymentStatusEnum.fromCode(pr.getStatus()).getLabel() : "未知");
        vo.setVoucherUrl(pr.getVoucherUrl());
        vo.setRemark(pr.getRemark());
        vo.setVerifyUser(pr.getVerifyUser());
        vo.setVerifyTime(pr.getVerifyTime());
        vo.setCancelUser(pr.getCancelUser());
        vo.setCancelTime(pr.getCancelTime());
        vo.setCancelReason(pr.getCancelReason());
        vo.setCreateTime(pr.getCreateTime());

        // user name
        if (pr.getUserId() != null) {
            User user = userMapper.selectById(pr.getUserId());
            if (user != null) vo.setUserName(user.getRealName() != null && !user.getRealName().isEmpty() ? user.getRealName() : user.getUsername());
        }
        if (pr.getVerifyUser() != null) {
            User user = userMapper.selectById(pr.getVerifyUser());
            if (user != null) vo.setVerifyUserName(user.getRealName() != null && !user.getRealName().isEmpty() ? user.getRealName() : user.getUsername());
        }
        if (pr.getCancelUser() != null) {
            User user = userMapper.selectById(pr.getCancelUser());
            if (user != null) vo.setCancelUserName(user.getRealName() != null && !user.getRealName().isEmpty() ? user.getRealName() : user.getUsername());
        }

        // 明细
        LambdaQueryWrapper<PaymentRecordBill> qw = new LambdaQueryWrapper<>();
        qw.eq(PaymentRecordBill::getPaymentRecordId, pr.getId());
        List<PaymentRecordBill> details = paymentRecordBillMapper.selectList(qw);
        List<PaymentRecordDetailVO> detailVOs = new ArrayList<>();
        for (PaymentRecordBill d : details) {
            PaymentRecordDetailVO dvo = new PaymentRecordDetailVO();
            dvo.setId(d.getId());
            dvo.setBillId(d.getBillId());
            dvo.setAmount(d.getAmount());
            Bill bill = billMapper.selectById(d.getBillId());
            if (bill != null) {
                dvo.setBillNo(bill.getBillNo());
                dvo.setFeeType(bill.getFeeType());
                dvo.setFeeTypeText(FeeTypeEnum.fromCode(bill.getFeeType()) != null
                    ? FeeTypeEnum.fromCode(bill.getFeeType()).getLabel() : "未知");
                dvo.setBillMonth(bill.getBillMonth());
                House house = assetMapper.selectById(bill.getHouseId());
                if (house != null) dvo.setHouseName(house.getName());
            }
            detailVOs.add(dvo);
        }
        vo.setBills(detailVOs);
        return vo;
    }

    // ======================== 物业费配置 ========================

    @Override
    public void setPropertyFeeConfig(PropertyFeeConfigDTO dto) {
        String effectiveMonth = LocalDate.now().plusMonths(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM"));

        PropertyFeeConfig config = new PropertyFeeConfig();
        config.setUnitPrice(dto.getUnitPrice());
        config.setEffectiveMonth(effectiveMonth);
        config.setStatus(1);
        config.setCreateTime(LocalDateTime.now());
        propertyFeeConfigMapper.insert(config);
    }

    @Override
    public PropertyFeeConfigVO getCurrentPropertyFeeConfig() {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        PropertyFeeConfig config = propertyFeeConfigMapper.getEffectiveConfig(currentMonth);
        if (config == null) {
            config = propertyFeeConfigMapper.getEffectiveConfig(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM")));
        }
        PropertyFeeConfigVO vo = new PropertyFeeConfigVO();
        if (config != null) {
            vo.setId(config.getId());
            vo.setUnitPrice(config.getUnitPrice());
            vo.setEffectiveMonth(config.getEffectiveMonth());
            vo.setStatus(config.getStatus());
            vo.setCreateTime(config.getCreateTime());
        }
        return vo;
    }
}
