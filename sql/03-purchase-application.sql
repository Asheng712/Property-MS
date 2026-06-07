-- ============================================
-- 智慧物业管理系统 — 资产购买/租赁申请表
-- ============================================

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS bus_purchase_application (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    application_no      VARCHAR(50)     NOT NULL UNIQUE COMMENT '申请编号',
    house_id            BIGINT          NOT NULL COMMENT '资产ID',
    applicant_id        BIGINT          NOT NULL COMMENT '申请人ID',
    applicant_name      VARCHAR(50)     DEFAULT '' COMMENT '申请人姓名',
    applicant_phone     VARCHAR(20)     DEFAULT '' COMMENT '申请人电话',
    type                VARCHAR(20)     DEFAULT 'PURCHASE' COMMENT '申请类型: PURCHASE=购买 RENTAL=租赁',
    status              TINYINT         DEFAULT 0 COMMENT '0=待审批 1=已通过 2=已拒绝',
    proposed_price      DECIMAL(10,2)   DEFAULT NULL COMMENT '建议价格(购买为售价,租赁为租金)',
    start_date          DATE            DEFAULT NULL COMMENT '合同开始日期(租赁)',
    end_date            DATE            DEFAULT NULL COMMENT '合同结束日期(租赁)',
    remark              VARCHAR(500)    DEFAULT '' COMMENT '备注/拒绝理由',
    created_contract_id BIGINT          DEFAULT NULL COMMENT '审批通过后生成的合同ID',
    create_time         DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (house_id) REFERENCES bus_house(id),
    FOREIGN KEY (applicant_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产购买/租赁申请表';

-- 为已有数据库添加 type 字段的迁移语句（如果表已存在）
-- ALTER TABLE bus_purchase_application ADD COLUMN type VARCHAR(20) DEFAULT 'PURCHASE' COMMENT '申请类型: PURCHASE=购买 RENTAL=租赁' AFTER applicant_phone;
