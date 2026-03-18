# 智慧物业管理系统 (WisdomPM) 数据库设计文档

## 1. 文档说明
本规范文档旨在明确智慧物业管理系统后台数据库的表结构、字段含义及关联关系，为后端 Java (Spring Boot) 开发及前端 React 联调提供统一的数据标准。

---

## 2. 模块详细设计

### 2.1 权限与用户模块 (RBAC)
该模块实现基于角色的访问控制（Role-Based Access Control），支撑系统的登录验证及功能权限划分。

#### 2.1.1 系统用户表 (`sys_user`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 主键ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 登录账号 |
| password | VARCHAR(255) | NOT NULL | 加密存储的密码 |
| real_name | VARCHAR(50) | - | 管理员真实姓名 |
| phone | VARCHAR(20) | - | 联系电话 |
| email | VARCHAR(100) | - | 电子邮箱 |
| avatar | VARCHAR(255) | - | 头像图片URL |
| role_id | BIGINT | FK | 关联 `sys_role.id` |
| status | TINYINT | DEFAULT 1 | 状态: 1-启用, 0-禁用 |
| create_time | DATETIME | DEFAULT NOW | 账号创建时间 |

#### 2.1.2 角色权限表 (`sys_role`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 角色ID |
| role_name | VARCHAR(50) | NOT NULL | 角色名称 (如: 超级管理员) |
| role_key | VARCHAR(50) | - | 权限字符 (如: admin) |
| permissions | TEXT | - | 权限JSON列表: ["asset:list", "bill:gen"] |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |

---

### 2.2 资产与合同模块 (Asset & Contract)
该模块管理物理资源（楼栋、商铺）及其经营租赁合同。

#### 2.2.1 资产表 (`bus_house`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 资产唯一标识 |
| parent_id | BIGINT | DEFAULT 0 | 树形结构父ID (0为顶级楼栋) |
| name | VARCHAR(100) | NOT NULL | 房号/铺号/楼栋名称 |
| type | ENUM | NOT NULL | BUILDING, UNIT, RESIDENTIAL, SHOP |
| area | DECIMAL(10,2) | - | 建筑面积 |
| status | ENUM | DEFAULT 'VACANT' | VACANT, SOLD, RENTING, DECORATING |
| owner_name | VARCHAR(50) | - | 业主或承租人姓名 |
| owner_phone | VARCHAR(20) | - | 联系方式 |
| create_time | DATETIME | DEFAULT NOW | 登记时间 |

#### 2.2.2 商业合同表 (`bus_contract`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 合同主键 |
| house_id | BIGINT | FK, NOT NULL | 关联 `bus_house.id` |
| tenant_name | VARCHAR(100) | - | 租户姓名或公司名称 |
| rent_amount | DECIMAL(10,2) | - | 每周期租金金额 |
| start_date | DATE | - | 合同生效日期 |
| end_date | DATE | - | 合同到期日期 |
| increase_rate | DECIMAL(5,2) | - | 年递增率百分比 |
| deposit | DECIMAL(10,2) | - | 押金金额 |
| contract_status| TINYINT | DEFAULT 1 | 1-有效, 0-过期/终止 |

---

### 2.3 财务与计费模块 (Finance)
该模块支撑智能账单生成及财务核销流水。

#### 2.3.1 账单批处理记录 (`bus_bill_batch`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 批次主键 |
| batch_no | VARCHAR(50) | UNIQUE | 业务批次号 (如: BAT-20240301) |
| fee_type | VARCHAR(50) | - | 费项类型 (物业费/租金/电费等) |
| target_range | VARCHAR(100) | - | 计费范围描述 (如: 全小区) |
| total_count | INT | - | 本次生成的账单总笔数 |
| total_amount | DECIMAL(12,2) | - | 本次生成账单总金额 |
| status | VARCHAR(20) | - | 处理状态: PROCESSING, COMPLETED |
| operator | VARCHAR(50) | - | 执行操作人账号 |
| create_time | DATETIME | DEFAULT NOW | 生成时间 |

#### 2.3.2 账单明细表 (`bus_bill`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 账单ID |
| batch_id | BIGINT | - | 关联 `bus_bill_batch.id` |
| house_id | BIGINT | FK, NOT NULL | 关联 `bus_house.id` |
| bill_no | VARCHAR(50) | UNIQUE | 唯一账单编号 |
| amount | DECIMAL(10,2) | NOT NULL | 应缴金额 |
| type | VARCHAR(20) | - | 费用具体科目 |
| pay_status | TINYINT | DEFAULT 0 | 0-未缴, 1-已缴 |
| deadline | DATE | - | 缴费最后期限 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |

#### 2.3.3 缴费流水表 (`bus_payment_record`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 流水ID |
| trx_no | VARCHAR(50) | UNIQUE | 交易流水号 (TRX-xxx) |
| bill_id | BIGINT | FK | 关联 `bus_bill.id` |
| house_id | BIGINT | FK | 关联房号 |
| pay_amount | DECIMAL(10,2) | NOT NULL | 实缴金额 |
| pay_type | ENUM | - | CASH, WECHAT, ALIPAY, TRANSFER |
| status | TINYINT | DEFAULT 0 | 0-待核销, 1-已核销 |
| pay_time | DATETIME | - | 缴费确认时间 |
| operator | VARCHAR(50) | - | 财务核销人姓名 |

---

### 2.4 服务模块 (Service)
支撑报修调度及投诉处理闭环逻辑。

#### 2.4.1 报修工单表 (`bus_repair`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 工单ID |
| repair_no | VARCHAR(50) | UNIQUE | 工单号 (REP-xxx) |
| house_id | BIGINT | - | 报修具体位置 |
| content | TEXT | NOT NULL | 故障描述摘要 |
| reporter | VARCHAR(50) | - | 报修人姓名 |
| worker_id | BIGINT | FK | 关联执行师傅 (`sys_user.id`) |
| status | TINYINT | DEFAULT 0 | 0-待处理, 1-处理中, 2-已办结 |
| priority | TINYINT | DEFAULT 1 | 1-普通, 2-紧急 |
| create_time | DATETIME | DEFAULT NOW | 创建时间 |
| finish_time | DATETIME | - | 办结归档时间 |

#### 2.4.2 投诉建议表 (`bus_complaint`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 投诉单ID |
| complaint_no | VARCHAR(50) | UNIQUE | 编号 (CP-xxx) |
| category | VARCHAR(50) | - | 分类 (噪音/卫生/违规等) |
| content | TEXT | NOT NULL | 投诉详细详情 |
| source | VARCHAR(100) | - | 来源区域或匿名状态 |
| status | TINYINT | DEFAULT 0 | 0-待处理, 1-处理中, 2-已办结 |
| create_time | DATETIME | DEFAULT NOW | 提起时间 |

---

### 2.5 公告与系统模块 (System)
支撑信息发布及异步文件处理任务。

#### 2.5.1 系统公告表 (`sys_notice`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 公告ID |
| title | VARCHAR(200) | NOT NULL | 公告标题 |
| content | LONGTEXT | - | HTML富文本内容 |
| target_type | VARCHAR(50) | - | 推送范围 (ALL/SHOP/RESIDENTIAL) |
| status | VARCHAR(20) | - | PUBLISHED, ARCHIVED |
| view_count | INT | DEFAULT 0 | 阅读次数统计 |
| create_time | DATETIME | DEFAULT NOW | 发布时间 |

#### 2.5.2 文件任务表 (`sys_file_task`)
| 字段名 | 类型 | 约束 | 备注 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PK, 自增 | 任务ID |
| task_type | ENUM | - | IMPORT (导入), EXPORT (导出) |
| file_name | VARCHAR(255) | - | 文件原始名称 |
| operator | VARCHAR(50) | - | 任务触发人 |
| data_count | INT | - | 涉及的数据行数 |
| status | ENUM | - | SUCCESS, FAIL, PROCESSING |
| file_url | VARCHAR(255) | - | 云存储或本地存储的下载URL |
| create_time | DATETIME | DEFAULT NOW | 任务启动时间 |

---

## 3. 设计约定
1. **金额计算**：所有金额字段必须使用 `DECIMAL` 类型，严禁使用 `float` 或 `double` 避免精度丢失。
2. **状态表示**：业务流程状态统一使用 `TINYINT`，并在代码中维护对应的 `Enum` 或 `Constant`。
3. **软删除**：如需实现逻辑删除，建议增加 `is_deleted` 字段，默认为 0。
4. **审计字段**：建议每张业务表在实际实施时统一增加 `update_time` 字段，以便于数据追踪。
