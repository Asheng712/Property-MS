-- ============================================
-- 缴费功能重构 — 数据库迁移
-- 日期: 2026-06-08
-- 说明: 按 payment-feature-design.md 重构账单与支付表
-- ============================================

SET NAMES utf8mb4;

-- -------------------------- 2.1 备份旧表 --------------------------
RENAME TABLE bus_bill TO bus_bill_old;
RENAME TABLE bus_payment_record TO bus_payment_record_old;

-- -------------------------- 2.2 新账单表 --------------------------
CREATE TABLE bus_bill (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT          NOT NULL COMMENT '用户ID',
    house_id    BIGINT          NOT NULL COMMENT '房屋ID',
    contract_id BIGINT          DEFAULT NULL COMMENT '合同ID',
    fee_type    TINYINT         NOT NULL COMMENT '费用类型: 1=租金 2=买房金额 3=押金 4=物业费',
    bill_no     VARCHAR(50)     NOT NULL UNIQUE COMMENT '账单编号',
    bill_month  VARCHAR(10)     DEFAULT NULL COMMENT '账单月份, 如2026-07',
    amount      DECIMAL(12,2)   NOT NULL COMMENT '应缴金额',
    status      TINYINT         DEFAULT 0 COMMENT '0=待缴费 1=待核销 2=已缴费 3=已撤销 4=已作废',
    due_date    DATE            DEFAULT NULL COMMENT '截止日期',
    remark      VARCHAR(500)    DEFAULT '' COMMENT '备注',
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_bill_user (user_id),
    INDEX idx_bill_house (house_id),
    INDEX idx_bill_contract (contract_id),
    INDEX idx_bill_month (bill_month),
    INDEX idx_bill_status (status),
    UNIQUE KEY uk_house_fee_month (house_id, fee_type, bill_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- -------------------------- 2.3 新支付记录表 --------------------------
CREATE TABLE bus_payment_record (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    payment_no      VARCHAR(50)     NOT NULL UNIQUE COMMENT '支付记录编号',
    user_id         BIGINT          NOT NULL COMMENT '缴费用户ID',
    amount          DECIMAL(12,2)   NOT NULL COMMENT '本次支付总金额',
    pay_method      TINYINT         NOT NULL COMMENT '支付方式: 1=微信 2=支付宝 3=银行卡 4=现金',
    pay_time        DATETIME        DEFAULT NULL COMMENT '用户支付时间',
    status          TINYINT         DEFAULT 0 COMMENT '0=待核销 1=已核销 2=已驳回 3=已撤销',
    voucher_url     VARCHAR(255)    DEFAULT '' COMMENT '支付凭证地址',
    remark          VARCHAR(500)    DEFAULT '' COMMENT '用户备注',
    verify_user     BIGINT          DEFAULT NULL COMMENT '核销管理员ID',
    verify_time     DATETIME        DEFAULT NULL COMMENT '核销时间',
    cancel_user     BIGINT          DEFAULT NULL COMMENT '撤销管理员ID',
    cancel_time     DATETIME        DEFAULT NULL COMMENT '撤销时间',
    cancel_reason   VARCHAR(500)    DEFAULT '' COMMENT '撤销原因',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_pr_user (user_id),
    INDEX idx_pr_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- -------------------------- 2.4 支付明细表 --------------------------
CREATE TABLE bus_payment_record_bill (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    payment_record_id   BIGINT          NOT NULL COMMENT '支付记录ID',
    bill_id             BIGINT          NOT NULL COMMENT '账单ID',
    amount              DECIMAL(12,2)   NOT NULL COMMENT '对应账单金额',
    create_time         DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_prb_payment (payment_record_id),
    INDEX idx_prb_bill (bill_id),
    FOREIGN KEY (payment_record_id) REFERENCES bus_payment_record(id),
    FOREIGN KEY (bill_id) REFERENCES bus_bill(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付明细表';

-- -------------------------- 2.5 物业费配置表 --------------------------
CREATE TABLE bus_property_fee_config (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    unit_price      DECIMAL(12,2)   NOT NULL COMMENT '每平方米每月单价',
    effective_month VARCHAR(10)     NOT NULL COMMENT '生效月份, 如2026-07',
    status          TINYINT         DEFAULT 1 COMMENT '0=禁用 1=启用',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_pfc_month (effective_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物业费配置表';

-- -------------------------- 2.6 默认物业费配置 --------------------------
INSERT INTO bus_property_fee_config (unit_price, effective_month, status) VALUES
(2.00, '2026-01', 1);

-- -------------------------- 2.7 迁移旧数据（demo data 不做全量迁移，仅保留结构备份） --------------------------
-- bus_bill_old 和 bus_payment_record_old 保留作为历史参考，后续可手动清理
