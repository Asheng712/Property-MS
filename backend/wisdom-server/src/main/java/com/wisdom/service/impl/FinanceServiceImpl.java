package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.BillGenerateDTO;
import com.wisdom.dto.BillPageQueryDTO;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.PaymentAuditDTO;
import com.wisdom.dto.PaymentPageQueryDTO;
import com.wisdom.entity.Bill;
import com.wisdom.entity.BillBatch;
import com.wisdom.entity.House;
import com.wisdom.entity.PaymentRecord;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.BillBatchMapper;
import com.wisdom.mapper.BillMapper;
import com.wisdom.mapper.PaymentRecordMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.FinanceService;
import com.wisdom.vo.BatchRecordVO;
import com.wisdom.vo.BillVO;
import com.wisdom.vo.PaymentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private BillBatchMapper billBatchMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private PaymentRecordMapper paymentRecordMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Override
    public BatchRecordVO batchGenerateBills(BillGenerateDTO billGenerateDTO) {
        BillBatch billBatch = new BillBatch();
        billBatch.setBatchNo("BAT-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        billBatch.setFeeType(billGenerateDTO.getFeeType());
        billBatch.setTargetRange(billGenerateDTO.getTargetRange());
        billBatch.setStatus("PROCESSING");
        billBatch.setOperator("admin");
        billBatch.setCreateTime(LocalDateTime.now());
        billBatchMapper.insert(billBatch);

        List<House> houses = assetMapper.selectAllAssets();
        int count = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (House house : houses) {
            Bill bill = new Bill();
            bill.setBatchId(billBatch.getId());
            bill.setHouseId(house.getId());
            bill.setBillNo("BILL-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + count);
            bill.setAmount(new BigDecimal(100));
            bill.setType(billGenerateDTO.getFeeType());
            bill.setPayStatus(0);
            bill.setDeadline(LocalDate.now().plusDays(30));
            bill.setCreateTime(LocalDateTime.now());
            billMapper.insert(bill);
            count++;
            totalAmount = totalAmount.add(bill.getAmount());
        }

        billBatch.setTotalCount(count);
        billBatch.setTotalAmount(totalAmount);
        billBatch.setStatus("COMPLETED");
        billBatchMapper.updateById(billBatch);

        BatchRecordVO batchRecordVO = new BatchRecordVO();
        BeanUtils.copyProperties(billBatch, batchRecordVO);
        return batchRecordVO;
    }

    @Override
    public PageResult<BatchRecordVO> getBatchLogs(PageQueryDTO pageQueryDTO) {
        Page<BillBatch> page = new Page<>(
            pageQueryDTO.getPage() != null ? pageQueryDTO.getPage() : 1,
            pageQueryDTO.getPageSize() != null ? pageQueryDTO.getPageSize() : 10
        );
        IPage<BillBatch> batchPage = billBatchMapper.selectPage(page, null);
        List<BatchRecordVO> batchRecordVOList = batchPage.getRecords().stream()
                .map(billBatch -> {
                    BatchRecordVO batchRecordVO = new BatchRecordVO();
                    BeanUtils.copyProperties(billBatch, batchRecordVO);
                    return batchRecordVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(batchPage.getTotal(), batchRecordVOList);
    }

    @Override
    public PageResult<BillVO> getBillList(BillPageQueryDTO billPageQueryDTO) {
        Page<Bill> page = new Page<>(
            billPageQueryDTO.getPage() != null ? billPageQueryDTO.getPage() : 1,
            billPageQueryDTO.getPageSize() != null ? billPageQueryDTO.getPageSize() : 10
        );
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        if (billPageQueryDTO.getBillNo() != null) {
            queryWrapper.like(Bill::getBillNo, billPageQueryDTO.getBillNo());
        }
        if (billPageQueryDTO.getHouseId() != null) {
            queryWrapper.eq(Bill::getHouseId, billPageQueryDTO.getHouseId());
        }
        if (billPageQueryDTO.getPayStatus() != null) {
            queryWrapper.eq(Bill::getPayStatus, billPageQueryDTO.getPayStatus());
        }
        if (billPageQueryDTO.getType() != null) {
            queryWrapper.eq(Bill::getType, billPageQueryDTO.getType());
        }
        IPage<Bill> billPage = billMapper.selectPage(page, queryWrapper);
        List<BillVO> billVOList = billPage.getRecords().stream()
                .map(bill -> {
                    BillVO billVO = new BillVO();
                    BeanUtils.copyProperties(bill, billVO);
                    House house = assetMapper.selectById(bill.getHouseId());
                    if (house != null) {
                        billVO.setHouseName(house.getName());
                    }
                    billVO.setPayStatusText(bill.getPayStatus() == 0 ? "未缴" : "已缴");
                    return billVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(billPage.getTotal(), billVOList);
    }

    @Override
    public void auditPayment(Long id, PaymentAuditDTO paymentAuditDTO) {
        PaymentRecord paymentRecord = paymentRecordMapper.selectById(id);
        if (paymentRecord == null) {
            throw new RuntimeException("缴费记录不存在");
        }
        paymentRecord.setStatus(paymentAuditDTO.getStatus());
        paymentRecord.setOperator(paymentAuditDTO.getOperator());
        paymentRecord.setPayTime(LocalDateTime.now());
        paymentRecordMapper.updateById(paymentRecord);

        Bill bill = billMapper.selectById(paymentRecord.getBillId());
        if (bill != null) {
            bill.setPayStatus(1);
            billMapper.updateById(bill);
        }
    }

    @Override
    public PageResult<PaymentVO> getPaymentList(PaymentPageQueryDTO paymentPageQueryDTO) {
        Page<PaymentRecord> page = new Page<>(
            paymentPageQueryDTO.getPage() != null ? paymentPageQueryDTO.getPage() : 1,
            paymentPageQueryDTO.getPageSize() != null ? paymentPageQueryDTO.getPageSize() : 10
        );
        LambdaQueryWrapper<PaymentRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (paymentPageQueryDTO.getTrxNo() != null) {
            queryWrapper.like(PaymentRecord::getTrxNo, paymentPageQueryDTO.getTrxNo());
        }
        if (paymentPageQueryDTO.getHouseId() != null) {
            queryWrapper.eq(PaymentRecord::getHouseId, paymentPageQueryDTO.getHouseId());
        }
        if (paymentPageQueryDTO.getStatus() != null) {
            queryWrapper.eq(PaymentRecord::getStatus, paymentPageQueryDTO.getStatus());
        }
        if (paymentPageQueryDTO.getPayType() != null) {
            queryWrapper.eq(PaymentRecord::getPayType, paymentPageQueryDTO.getPayType());
        }
        IPage<PaymentRecord> paymentPage = paymentRecordMapper.selectPage(page, queryWrapper);
        List<PaymentVO> paymentVOList = paymentPage.getRecords().stream()
                .map(paymentRecord -> {
                    PaymentVO paymentVO = new PaymentVO();
                    BeanUtils.copyProperties(paymentRecord, paymentVO);
                    House house = assetMapper.selectById(paymentRecord.getHouseId());
                    if (house != null) {
                        paymentVO.setHouseName(house.getName());
                    }
                    Bill bill = billMapper.selectById(paymentRecord.getBillId());
                    if (bill != null) {
                        paymentVO.setBillNo(bill.getBillNo());
                    }
                    paymentVO.setStatusText(paymentRecord.getStatus() == 0 ? "待核销" : "已核销");
                    return paymentVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(paymentPage.getTotal(), paymentVOList);
    }
}