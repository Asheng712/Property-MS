# 物业管理系统 - 后端技术说明文档

## 1. 总体架构设计
本系统后端采用基于 **Java 17** 的 **Spring Boot 3.x** 框架，遵循**前后端分离**架构。
*   **表现层 (Controller):** 负责处理 RESTful API 请求，进行参数校验。
*   **业务逻辑层 (Service):** 处理核心业务逻辑（如费用计算、报修流程状态流转）。
*   **持久层 (Mapper/DAO):** 利用 **MyBatis-Plus** 实现高效的数据库 CRUD 操作。
*   **安全层 (Security):** 基于 **Spring Security + JWT** 实现无状态认证与权限控制。

## 2. 技术栈详细清单
*   **核心框架:** Spring Boot 3.2.x
*   **数据库访问:** MyBatis-Plus 3.5.x
*   **数据库:** MySQL 8.0
*   **权限校验:** Spring Security, JWT (JSON Web Token)
*   **工具库:** Lombok (代码精简), Hutool (工具类), EasyExcel (报表导出)
*   **接口文档:** Knife4j (Swagger 3)
*   **依赖管理:** Maven

## 3. 数据库设计 (ER 模型摘要)
后端共设计 10 张核心表，按模块划分如下：

**权限与用户模块:**
1. **sys_user** — 系统用户（管理员/业主账号、密码、手机号、角色）
2. **sys_role** — 角色权限表

**资产与合同模块:**
3. **bus_house** — 房屋资产（楼栋、单元、房号、面积、业主）
4. **bus_contract** — 商业合同（租户、租金、租期）

**财务与计费模块:**
5. **bus_bill_batch** — 账单批处理记录
6. **bus_bill** — 账单明细（金额、支付状态: 0-未缴/1-已缴、截止日期）
7. **bus_payment_record** — 缴费流水（交易号、实缴金额、支付方式、核销状态）

**服务模块:**
8. **bus_repair** — 报修工单（故障描述、优先级、处理状态: 待处理/处理中/已办结）
9. **bus_complaint** — 投诉建议（分类、内容、来源、处理结果）

**系统模块:**
10. **sys_notice** — 系统公告（标题、内容、发布范围、阅读量）
11. **sys_file_task** — 文件导入导出任务记录

## 4. 核心功能实现逻辑

### 4.1 认证与授权 (JWT 流程)
1.  用户登录成功后，后端生成包含 `userId` 和 `role` 的 JWT 字符串。
2.  后续请求需在 Header 中携带 `Authorization: Bearer <Token>`。
3.  后端通过自定义 `JwtAuthenticationFilter` 拦截器校验合法性，并将用户信息存入 `SecurityContext`。

### 4.2 响应式数据支持
为了适配前端的响应式布局，后端接口统一采用**分页插件 (MyBatis-Plus Pagination)**。
*   管理员端：返回全量数据分页，支持多条件搜索。
*   业主端：接口通过 `BaseContext.getCurrentId()` 获取当前登录用户ID，自动过滤账单/缴费/报修/投诉/合同数据，实现”仅看个人相关数据”的行级权限控制。创建报修时校验房屋归属，非名下房屋拒绝提交。`@LoginRequired` 注解保护用户端接口。

### 4.3 费用自动计算
系统内置单价配置逻辑。例如：`账单金额 = 房屋面积 * 物业费单价 + 抄表读数`。后端通过定时任务（或手动触发）批量插入 `bus_bill` 记录。

## 5. 接口规范与异常处理

### 5.1 统一响应格式
所有接口均返回封装类 `Result<T>`：
```json
{
  "code": 200,      // 状态码
  "message": "success", 
  "data": { ... }   // 业务数据
}
```

### 5.2 全局异常处理
利用 `@RestControllerAdvice` 捕获业务异常（如 `UserNotFoundException`）和系统异常，防止将堆栈信息暴露给前端，提高系统鲁棒性。

## 6. 环境运行要求
*   **JDK:** 17 及以上
*   **Maven:** 3.8+
*   **MySQL:** 8.0+
*   **IDE建议:** IntelliJ IDEA

---

**文档说明：** 本文档配合 Knife4j 自动生成的 API 接口文档使用，可实现后端接口的可视化测试与前后端高效协作。
