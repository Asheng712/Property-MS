-- ============================================
-- 智慧物业管理系统 — 数据库初始化 (基于 docs/database.md)
-- ============================================

SET NAMES utf8mb4;

-- -------------------------- 2.1 权限与用户模块 --------------------------

CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    role_name   VARCHAR(50)     NOT NULL,
    role_key    VARCHAR(50)     NOT NULL UNIQUE,
    permissions TEXT,
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    real_name   VARCHAR(50)     DEFAULT '',
    phone       VARCHAR(20)     DEFAULT '',
    email       VARCHAR(100)    DEFAULT '',
    avatar      VARCHAR(255)    DEFAULT '',
    role_id     BIGINT          DEFAULT NULL,
    status      TINYINT         DEFAULT 1,
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- -------------------------- 2.2 资产与合同模块 --------------------------

CREATE TABLE IF NOT EXISTS bus_house (
    id          BIGINT              AUTO_INCREMENT PRIMARY KEY,
    parent_id   BIGINT              DEFAULT 0,
    name        VARCHAR(100)        NOT NULL,
    type        ENUM('BUILDING','UNIT','RESIDENTIAL','SHOP') NOT NULL,
    area        DECIMAL(10,2)       DEFAULT NULL,
    status      ENUM('VACANT','SOLD','RENTING','DECORATING','OCCUPIED') DEFAULT 'VACANT',
    owner_name  VARCHAR(50)         DEFAULT '',
    owner_phone VARCHAR(20)         DEFAULT '',
    create_time DATETIME            DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产表';

CREATE TABLE IF NOT EXISTS bus_contract (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    house_id        BIGINT          DEFAULT NULL,
    tenant_name     VARCHAR(100)    DEFAULT '',
    rent_amount     DECIMAL(10,2)   DEFAULT 0.00,
    start_date      DATE            DEFAULT NULL,
    end_date        DATE            DEFAULT NULL,
    increase_rate   DECIMAL(5,2)    DEFAULT 0.00,
    deposit         DECIMAL(10,2)   DEFAULT 0.00,
    contract_status TINYINT         DEFAULT 1,
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (house_id) REFERENCES bus_house(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商业合同表';

-- -------------------------- 2.3 财务与计费模块 --------------------------

CREATE TABLE IF NOT EXISTS bus_bill_batch (
    id           BIGINT          AUTO_INCREMENT PRIMARY KEY,
    batch_no     VARCHAR(50)     NOT NULL UNIQUE,
    fee_type     VARCHAR(50)     DEFAULT '',
    target_range VARCHAR(100)    DEFAULT '',
    total_count  INT             DEFAULT 0,
    total_amount DECIMAL(12,2)   DEFAULT 0.00,
    status       VARCHAR(20)     DEFAULT '',
    operator     VARCHAR(50)     DEFAULT '',
    create_time  DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单批处理记录';

CREATE TABLE IF NOT EXISTS bus_bill (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    batch_id    BIGINT          DEFAULT NULL,
    house_id    BIGINT          NOT NULL,
    bill_no     VARCHAR(50)     NOT NULL UNIQUE,
    amount      DECIMAL(10,2)   NOT NULL,
    type        VARCHAR(20)     DEFAULT '',
    pay_status  TINYINT         DEFAULT 0,
    deadline    DATE            DEFAULT NULL,
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单明细表';

CREATE TABLE IF NOT EXISTS bus_payment_record (
    id          BIGINT              AUTO_INCREMENT PRIMARY KEY,
    trx_no      VARCHAR(50)         NOT NULL UNIQUE,
    bill_id     BIGINT              DEFAULT NULL,
    house_id    BIGINT              DEFAULT NULL,
    pay_amount  DECIMAL(10,2)       NOT NULL,
    pay_type    ENUM('CASH','WECHAT','ALIPAY','TRANSFER') DEFAULT NULL,
    status      TINYINT             DEFAULT 0,
    pay_time    DATETIME            DEFAULT NULL,
    operator    VARCHAR(50)         DEFAULT '',
    create_time DATETIME            DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费流水表';

-- -------------------------- 2.4 服务模块 --------------------------

CREATE TABLE IF NOT EXISTS bus_repair (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    repair_no   VARCHAR(50)     NOT NULL UNIQUE,
    house_id    BIGINT          DEFAULT NULL,
    content     TEXT            NOT NULL,
    reporter    VARCHAR(50)     DEFAULT '',
    worker_id   BIGINT          DEFAULT NULL,
    status      TINYINT         DEFAULT 0,
    priority    TINYINT         DEFAULT 1,
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP,
    finish_time DATETIME        DEFAULT NULL,
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (worker_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修工单表';

CREATE TABLE IF NOT EXISTS bus_complaint (
    id            BIGINT        AUTO_INCREMENT PRIMARY KEY,
    complaint_no  VARCHAR(50)   NOT NULL UNIQUE,
    category      VARCHAR(50)   DEFAULT '',
    content       TEXT          NOT NULL,
    source        VARCHAR(100)  DEFAULT '',
    status        TINYINT       DEFAULT 0,
    handle_result VARCHAR(500)  DEFAULT '',
    create_time   DATETIME      DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投诉建议表';

-- -------------------------- 2.5 公告与系统模块 --------------------------

CREATE TABLE IF NOT EXISTS sys_notice (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200)    NOT NULL,
    content     LONGTEXT,
    target_type VARCHAR(50)     DEFAULT '',
    status      VARCHAR(20)     DEFAULT '',
    view_count  INT             DEFAULT 0,
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

CREATE TABLE IF NOT EXISTS sys_file_task (
    id          BIGINT              AUTO_INCREMENT PRIMARY KEY,
    task_type   ENUM('IMPORT','EXPORT') DEFAULT NULL,
    file_name   VARCHAR(255)        DEFAULT '',
    operator    VARCHAR(50)         DEFAULT '',
    data_count  INT                 DEFAULT 0,
    status      ENUM('SUCCESS','FAIL','PROCESSING') DEFAULT NULL,
    file_url    VARCHAR(255)        DEFAULT '',
    create_time DATETIME            DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件任务表';

-- ============================================
-- 初始数据
-- ============================================

INSERT INTO sys_role (id, role_name, role_key, permissions) VALUES
(1, '超级管理员', 'admin', '["*"]'),
(2, '普通用户',   'user',  '[]');

INSERT INTO sys_user (id, username, password, real_name, role_id, status) VALUES
(1, 'admin', '$2b$10$ItG3jyKWSusKaDAtcBeOFOwjZLHJg1zj0VtXYw8Xm/NtKkxwMOFc2', '系统管理员', 1, 1);

-- 基础资产数据
INSERT INTO bus_house (id, parent_id, name, type, area, status, owner_name, owner_phone) VALUES
(1, 0, 'A栋综合楼',        'BUILDING',    5000.00, 'OCCUPIED', NULL,   NULL),
(2, 1, '101商铺',          'SHOP',         120.50, 'OCCUPIED', '张三', '13800001111'),
(3, 1, '102商铺',          'SHOP',          98.00, 'VACANT',   NULL,   NULL),
(4, 1, '201住宅',          'RESIDENTIAL',   89.50, 'SOLD',     '李四', '13800002222'),
(5, 0, 'B栋综合楼',        'BUILDING',    3800.00, 'OCCUPIED', NULL,   NULL),
(6, 5, '101办公楼',        'UNIT',         200.00, 'RENTING',  '王五', '13800003333'),
(7, 5, '202住宅',          'RESIDENTIAL',   76.00, 'VACANT',   NULL,   NULL);
