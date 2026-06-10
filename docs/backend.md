# 物业管理系统 — 后端技术说明文档

## 1. 总体架构
基于 **Java 17 + Spring Boot 3.x** 的前后分离架构，Maven 多模块管理。

| 模块 | 职责 |
|:---|:---|
| `wisdom-common` | 工具类、枚举（FeeTypeEnum/BillStatusEnum/PaymentStatusEnum/PayMethodEnum）、异常、Result 封装 |
| `wisdom-pojo` | Entity、DTO、VO 数据对象，不含业务逻辑 |
| `wisdom-server` | 核心业务：Controller、Service、Mapper、Filter、Handler |

## 2. 技术栈
- **核心**: Spring Boot 3.2.x, MyBatis-Plus 3.5.x
- **数据库**: MySQL 8.4
- **认证**: JWT + Spring Security（无状态）
- **工具**: Lombok, Knife4j (Swagger 3)
- **测试**: JUnit 5 + Mockito（89 个测试）
- **构建**: Maven 3.9, Docker Compose

## 3. 数据库（14 张核心表）

| 模块 | 表名 | 说明 |
|:---|:---|:---|
| 权限 | `sys_user`, `sys_role` | 用户与角色 |
| 资产 | `bus_house`, `bus_contract`, `bus_purchase_application` | 房屋、合同、购买租赁申请 |
| 财务 | `bus_bill`, `bus_payment_record`, `bus_payment_record_bill`, `bus_property_fee_config` | 账单、支付、物业费定价 |
| 服务 | `bus_repair`, `bus_complaint` | 报修、投诉 |
| 系统 | `sys_notice`, `sys_file_task` | 公告、导入导出 |

## 4. 核心功能实现

### 4.1 缴费流程（重构后）
```
合同审批通过 → 自动生成当月账单 (rent+purchase+deposit+property)
用户选择多账单 → 提交缴费 (PaymentSubmitDTO) → 支付记录 status=0
管理员核销     → verifyPayment() → 支付记录 status=1, 账单 status=2
管理员撤销     → cancelPayment() → 支付记录 status=3, 账单 status=0
```
- 支持多账单合并支付（`bus_payment_record_bill` 关联表）
- 核销可撤销，撤销后账单回退为待缴费
- 物业费调价从下月生效，历史账单金额不变

### 4.2 账单自动生成
- 合同审批通过（`PurchaseApplicationServiceImpl.approve`）→ 自动调用 `FinanceService.generateBillsSafe`
- 直接新增合同（`ContractServiceImpl.saveOrUpdateContract`）→ 同上
- `generateBillsSafe` 无事务标注，共享外层事务，异常不回滚外层
- 账单 `due_date` = 对应月份的月末（`YearMonth.parse(billMonth).atEndOfMonth()`）
- `bill_no` 含 feeType + 毫秒时间戳避免并发冲突

### 4.3 数据权限隔离
- 管理员：全量数据
- 普通用户：通过 `UserService.isCurrentUserAdmin()` 判断，非admin自动过滤名下房屋数据
- `BaseContext` (ThreadLocal) 存储当前登录用户ID
- `@LoginRequired` 注解保护用户端接口

### 4.4 全局异常处理
- `GlobalExceptionHandler` 统一捕获 `BusinessException`（code 映射到 HTTP status）
- `BusinessException` 工厂方法: `notFound()`→404, `badRequest()`→400, `unauthorized()`→401, `forbidden()`→403

## 5. 接口规范
- 统一返回 `Result<T>`: `{ code, msg, data }`
- 分页返回 `PageResult<T>`: `{ total, records }`
- 分页参数: `page`(从1开始), `pageSize`

## 6. 运行要求
- JDK 17+, Maven 3.8+, MySQL 8.0+
- 开发启动: `cp .env.example .env && docker compose up -d --build`
- 生产部署: `docker compose -f compose.prod.yaml up -d --build`

---

**更新时间**: 2026-06-08 — 同步 v2.1.2 重构后的缴费逻辑与账单生成机制
