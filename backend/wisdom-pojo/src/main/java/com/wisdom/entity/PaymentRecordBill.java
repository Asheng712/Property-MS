package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bus_payment_record_bill")
public class PaymentRecordBill implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long paymentRecordId;
    private Long billId;
    private BigDecimal amount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
