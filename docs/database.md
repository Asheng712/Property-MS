# 智慧物业管理系统 (WisdomPM) 数据库设计文档

## 1. 文档说明
本规范文档旨在明确智慧物业管理系统后台数据库的表结构、字段含义及关联关系，为后端 Java (Spring Boot) 开发及前端 Vue 联调提供统一的数据标准。

---

## 2. 模块详细设计

### 2.1 权限与用户模块 (RBAC)

#### 2.1.1 系统用户表 (`sys_user`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 主键ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 登录账号 |
| password | VARCHAR(255) | NOT NULL | Bcrypt 加密存储 |
| real_name | VARCHAR(50) | - | 真实姓名 |
| phone | VARCHAR(20) | - | 联系电话 |
| email | VARCHAR(100) | - | 电子邮箱 |
| avatar | VARCHAR(255) | - | 头像图片URL |
| role_id | BIGINT | FK | 关联 `sys_role.id` |
| status | TINYINT | DEFAULT 1 | 1-启用, 0-禁用 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

#### 2.1.2 角色权限表 (`sys_role`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 角色ID |
| role_name | VARCHAR(50) | NOT NULL | 角色名称 |
| role_key | VARCHAR(50) | UNIQUE | 权限字符 (admin, user) |
| permissions | TEXT | - | 权限JSON: `["asset:list", "bill:gen"]` |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

---

### 2.2 资产与合同模块 (Asset & Contract)

#### 2.2.1 资产表 (`bus_house`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 资产唯一标识 |
| parent_id | BIGINT | DEFAULT 0 | 树形父ID (0为顶级楼栋) |
| name | VARCHAR(100) | NOT NULL | 房号/铺号/楼栋名称 |
| type | ENUM | NOT NULL | BUILDING, UNIT, RESIDENTIAL, SHOP |
| area | DECIMAL(10,2) | - | 建筑面积 |
| status | ENUM | DEFAULT 'VACANT' | VACANT, SOLD, RENTING, DECORATING, OCCUPIED |
| owner_name | VARCHAR(50) | - | 业主/承租人姓名 |
| owner_phone | VARCHAR(20) | - | 联系方式 |
| owner_id | BIGINT | FK→sys_user.id | 产权人系统用户ID |
| create_time | DATETIME | DEFAULT NOW | 登记时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

#### 2.2.2 商业合同表 (`bus_contract`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 合同主键 |
| house_id | BIGINT | FK, NOT NULL | 关联 `bus_house.id` |
| tenant_name | VARCHAR(100) | - | 租户姓名/公司名称 |
| rent_amount | DECIMAL(10,2) | - | 每周期租金（购买合同为总价） |
| start_date | DATE | - | 合同生效日期 |
| end_date | DATE | - | 合同到期日期（购买合同为NULL） |
| increase_rate | DECIMAL(5,2) | - | 年递增率百分比 |
| deposit | DECIMAL(10,2) | - | 押金（>0 为租赁，=0 为购买） |
| contract_status| TINYINT | DEFAULT 1 | 1-有效, 0-过期/终止 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

#### 2.2.3 资产购买申请表 (`bus_purchase_application`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 申请ID |
| application_no | VARCHAR(50) | UNIQUE | 申请编号 (APP-xxx) |
| type | VARCHAR(20) | - | PURCHASE / RENTAL |
| house_id | BIGINT | FK | 申请购买的资产 |
| applicant_id | BIGINT | FK→sys_user.id | 申请人用户ID |
| applicant_name | VARCHAR(50) | - | 申请人姓名 |
| applicant_phone | VARCHAR(20) | - | 联系电话 |
| status | TINYINT | DEFAULT 0 | 0-待审批, 1-已通过, 2-已拒绝 |
| proposed_price | DECIMAL(12,2) | - | 审批定价 |
| start_date | DATE | - | 租赁合同开始日期 |
| end_date | DATE | - | 租赁合同结束日期 |
| created_contract_id | BIGINT | - | 审批通过后生成的合同ID |
| remark | VARCHAR(500) | - | 备注/拒绝理由 |
| create_time | DATETIME | DEFAULT NOW | 申请时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

---

### 2.3 财务与计费模块 (Finance)

#### 2.3.1 账单表 (`bus_bill`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 账单ID |
| user_id | BIGINT | NOT NULL | 应收用户ID |
| house_id | BIGINT | NOT NULL | 关联资产 |
| contract_id | BIGINT | - | 关联合同 |
| fee_type | TINYINT | NOT NULL | 1-租金, 2-买房金额, 3-押金, 4-物业费 |
| bill_no | VARCHAR(50) | UNIQUE | 唯一账单编号 |
| bill_month | VARCHAR(10) | - | 账期月份 (如 2026-06) |
| amount | DECIMAL(12,2) | NOT NULL | 应缴金额 |
| status | TINYINT | DEFAULT 0 | 0-待缴费, 1-待核销, 2-已缴费, 3-已撤销, 4-已作废 |
| due_date | DATE | - | 缴费截止日期（合同当月月末） |
| remark | VARCHAR(500) | - | 备注 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

唯一约束: `uk_house_fee_month` (house_id, fee_type, bill_month)

#### 2.3.2 支付记录表 (`bus_payment_record`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 支付记录ID |
| payment_no | VARCHAR(50) | UNIQUE | 支付流水号 (PAY-xxx) |
| user_id | BIGINT | NOT NULL | 支付用户ID |
| amount | DECIMAL(12,2) | NOT NULL | 支付总金额 |
| pay_method | TINYINT | NOT NULL | 1-微信, 2-支付宝, 3-银行卡, 4-现金 |
| pay_time | DATETIME | - | 支付时间 |
| status | TINYINT | DEFAULT 0 | 0-待核销, 1-已核销, 2-已驳回, 3-已撤销 |
| voucher_url | VARCHAR(255) | - | 支付凭证图片URL |
| remark | VARCHAR(500) | - | 备注 |
| verify_user | BIGINT | - | 核销人ID |
| verify_time | DATETIME | - | 核销时间 |
| cancel_user | BIGINT | - | 撤销人ID |
| cancel_time | DATETIME | - | 撤销时间 |
| cancel_reason | VARCHAR(500) | - | 撤销原因 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

#### 2.3.3 支付明细表 (`bus_payment_record_bill`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 明细ID |
| payment_record_id | BIGINT | FK | 关联支付记录 |
| bill_id | BIGINT | FK | 关联账单 |
| amount | DECIMAL(12,2) | NOT NULL | 该笔账单支付金额 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

支持多账单合并支付：一次支付可关联多条账单。

#### 2.3.4 物业费配置表 (`bus_property_fee_config`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 配置ID |
| unit_price | DECIMAL(12,2) | NOT NULL | 单价 (元/㎡) |
| effective_month | VARCHAR(10) | NOT NULL | 生效月份 (如 2026-01) |
| status | TINYINT | DEFAULT 1 | 0-禁用, 1-启用 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

物业费 = 房屋面积 × 单价。调价从下月生效，历史账单金额不变。

---

### 2.4 服务模块 (Service)

#### 2.4.1 报修工单表 (`bus_repair`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 工单ID |
| repair_no | VARCHAR(50) | UNIQUE | 工单号 (REP-xxx) |
| house_id | BIGINT | - | 报修房屋 |
| content | TEXT | NOT NULL | 故障描述 |
| reporter | VARCHAR(50) | - | 报修人姓名 |
| reporter_id | BIGINT | FK→sys_user.id | 报修人用户ID |
| worker_id | BIGINT | FK→sys_user.id | 执行师傅 |
| status | TINYINT | DEFAULT 0 | 0-待处理, 1-处理中, 2-已办结 |
| priority | TINYINT | DEFAULT 1 | 1-普通, 2-紧急, 3-非常紧急 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| finish_time | DATETIME | - | 办结时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

#### 2.4.2 投诉建议表 (`bus_complaint`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 投诉单ID |
| complaint_no | VARCHAR(50) | UNIQUE | 编号 (CP-xxx) |
| category | VARCHAR(50) | - | 分类 |
| content | TEXT | NOT NULL | 投诉内容 |
| source | VARCHAR(100) | - | 来源 |
| reporter_id | BIGINT | FK→sys_user.id | 提交人用户ID |
| status | TINYINT | DEFAULT 0 | 0-待处理, 1-处理中, 2-已办结 |
| handle_result | VARCHAR(500) | - | 处理结果 |
| create_time | DATETIME | DEFAULT NOW | 提起时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

---

### 2.5 公告与系统模块 (System)

#### 2.5.1 系统公告表 (`sys_notice`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 公告ID |
| title | VARCHAR(200) | NOT NULL | 公告标题 |
| content | LONGTEXT | - | HTML富文本内容 |
| target_type | VARCHAR(50) | - | 推送范围 |
| status | VARCHAR(20) | - | PUBLISHED / ARCHIVED |
| view_count | INT | DEFAULT 0 | 阅读次数 |
| create_time | DATETIME | DEFAULT NOW | 发布时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

#### 2.5.2 文件任务表 (`sys_file_task`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 任务ID |
| task_type | ENUM | - | IMPORT / EXPORT |
| file_name | VARCHAR(255) | - | 文件名 |
| operator | VARCHAR(50) | - | 操作人 |
| data_count | INT | - | 数据行数 |
| status | ENUM | - | SUCCESS, FAIL, PROCESSING |
| file_url | VARCHAR(255) | - | 下载URL |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| update_time | DATETIME | ON UPDATE NOW | 更新时间 |

---

## 3. ER 关系图

```
sys_user ──┬── bus_house ──┬── bus_contract
           │               │
           │               ├── bus_bill ──── bus_payment_record_bill ──── bus_payment_record
           │               │
           ├── bus_repair   ├── bus_property_fee_config
           ├── bus_complaint
           └── bus_purchase_application
```

## 4. 设计约定
1. **金额**: 必须使用 `DECIMAL(12,2)`，严禁 `float`/`double`
2. **状态**: 统一使用 `TINYINT`，Java 端维护对应 Enum
3. **审计**: 每张业务表含 `create_time` + `update_time`
4. **缴费流程**: 用户选择多账单全额支付 → 支付记录 status=0 → 管理员核销 → 账单 status=2 + 支付记录 status=1；核销可撤销
5. **账单生成**: 合同审批通过后自动生成当月租金/买房+押金+物业费账单，due_date 取账单月份月末
