package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bus_payment_record")
public class PaymentRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String trxNo;
    private Long billId;
    private Long houseId;
    private BigDecimal payAmount;
    private String payType;
    private Integer status;
    private LocalDateTime payTime;
    private String operator;
}