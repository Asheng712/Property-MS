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
后端共设计 5 张核心表，以满足物业管理需求：

1.  **sys_user (用户表):** 存储管理员及业主账号、加密密码、手机号及角色。
2.  **bus_house (房屋表):** 记录楼栋、单元、房号、建筑面积及关联的业主 ID。
3.  **bus_bill (账单表):** 记录物业费、水电气费。包含金额、账单周期、支付状态（0-未缴, 1-已缴）。
4.  **bus_repair (报修表):** 记录报修内容、图片路径、当前状态（待处理、处理中、已完成）。
5.  **sys_notice (公告表):** 存储物业发布的社区通知及发布时间。

## 4. 核心功能实现逻辑

### 4.1 认证与授权 (JWT 流程)
1.  用户登录成功后，后端生成包含 `userId` 和 `role` 的 JWT 字符串。
2.  后续请求需在 Header 中携带 `Authorization: Bearer <Token>`。
3.  后端通过自定义 `JwtAuthenticationFilter` 拦截器校验合法性，并将用户信息存入 `SecurityContext`。

### 4.2 响应式数据支持
为了适配前端的响应式布局，后端接口统一采用**分页插件 (MyBatis-Plus Pagination)**。
*   管理员端：返回全量数据分页，支持多条件搜索。
*   业主端：接口通过 `SecurityContext` 获取当前登录 ID，实现“仅看个人相关数据”的行级权限控制。

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
