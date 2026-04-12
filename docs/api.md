# 智慧物业管理系统 (WisdomPM) API 接口设计文档

## 1. 通用说明

### 1.1 基础信息

* **网关地址**: `http://localhost:8080/api/v1`
* **数据格式**: `Content-Type: application/json`
* **认证方式**: 请求头携带 `Authorization: Bearer <JWT_TOKEN>`

### 1.2 统一返回格式 (`Result<T>`)

所有接口必须返回以下结构的 JSON：

```json
{
  "code": 200,      // 业务状态码: 200-成功, 401-未登录, 403-无权限, 500-系统异常
  "msg": "操作成功", // 提示信息
  "data": {}        // 具体的业务数据 (Object 或 Array)
}
```

---

## 2. 模块 API 设计

### 2.1 认证与权限模块 (Auth & RBAC)

| 功能       | 方法     | 路径                  | DTO (入参)            | VO (出参)              |
|:-------- |:------ |:------------------- |:------------------- |:-------------------- |
| 用户登录     | `POST` | `/auth/login`       | `UserLoginDTO`      | `String (Token)`     |
| 用户注册     | `POST` | `/auth/register`    | `UserRegisterDTO`   | -                    |
| 获取当前用户信息 | `GET`  | `/auth/info`        | -                   | `UserVO`             |
| 分页获取角色列表 | `GET`  | `/roles`            | `PageQueryDTO`      | `PageResult<RoleVO>` |
| 更新角色权限   | `PUT`  | `/roles/{id}/perms` | `RolePermUpdateDTO` | -                    |

---

### 2.2 资产与合同模块 (Asset & Contract)

| 功能       | 方法         | 路径             | DTO (入参) | VO (出参)                  |
|:-------- |:---------- |:-------------- |:------------------------------------ |:------------------------ |
| 获取资产空间树  | `GET`      | `/assets/tree` | -                                    | `List<AssetTreeVO>`      |
| 分页查询资产列表 | `GET`      | `/assets`      | `AssetPageQueryDTO`                  | `PageResult<AssetVO>`    |
| 新增/编辑资产  | `POST/PUT` | `/assets`      | `AssetDTO`                           | -                        |
| 删除资产     | `DELETE`   | `/assets/{id}` | -                                    | -                        |
| 分页查询合同   | `GET`      | `/contracts`   | `ContractPageQueryDTO`               | `PageResult<ContractVO>` |
| 新增/终止合同  | `POST/PUT` | `/contracts`   | `ContractDTO`                        | -                        |

---

### 2.3 财务与计费模块 (Finance & Billing)

| 功能        | 方法     | 路径                      | DTO (入参)              | VO (出参)                     |
|:--------- |:------ |:----------------------- |:--------------------- |:--------------------------- |
| 批量生成月度账单  | `POST` | `/bills/batch-generate` | `BillGenerateDTO`     | `BatchRecordVO`             |
| 查询账单批处理历史 | `GET`  | `/bills/batch-logs`     | `PageQueryDTO`        | `PageResult<BatchRecordVO>` |
| 分页查询账单明细  | `GET`  | `/bills`                | `BillPageQueryDTO`    | `PageResult<BillVO>`        |
| 财务流水核销确认  | `PUT`  | `/finance/audit/{id}`   | `PaymentAuditDTO`     | -                           |
| 分页查询缴费流水  | `GET`  | `/finance/payments`     | `PaymentPageQueryDTO` | `PageResult<PaymentVO>`     |

---

### 2.4 服务模块 (Repair & Complaint)

| 功能        | 方法     | 路径                   | DTO (入参)                | VO (出参)                   |
|:--------- |:------ |:-------------------- |:----------------------- |:------------------------- |
| 获取报修看板数据  | `GET`  | `/repairs/kanban`    | -                       | `RepairKanbanVO`          |
| 录入/代录工单   | `POST` | `/repairs`           | `RepairDTO`             | -                         |
| 派发工单/指派师傅 | `PUT`  | `/repairs/dispatch`  | `RepairDispatchDTO`     | -                         |
| 更新工单状态    | `PUT`  | `/repairs/status`    | `RepairStatusUpdateDTO` | -                         |
| 分页查询投诉建议  | `GET`  | `/complaints`        | `ComplaintPageQueryDTO` | `PageResult<ComplaintVO>` |
| 处理并反馈投诉   | `PUT`  | `/complaints/handle` | `ComplaintHandleDTO`    | -                         |

---

### 2.5 公告与系统模块 (Notice & System)

| 功能       | 方法     | 路径                       | DTO (入参)             | VO (出参)                  |
|:-------- |:------ |:------------------------ |:-------------------- |:------------------------ |
| 分页查询公告历史 | `GET`  | `/notices`               | `NoticePageQueryDTO` | `PageResult<NoticeVO>`   |
| 发布/暂存公告  | `POST` | `/notices`               | `NoticeDTO`          | -                        |
| 获取导入导出任务 | `GET`  | `/system/tasks`          | `PageQueryDTO`       | `PageResult<FileTaskVO>` |
| 导出财务报表   | `GET`  | `/system/export/finance` | `ReportExportDTO`    | `File (Resource)`        |
| 导入资产数据   | `POST` | `/system/import/assets`  | `MultipartFile`      | `ImportResultVO`         |

---

### ER图

<img width="968" height="894" alt="image" src="https://github.com/user-attachments/assets/8c24617b-7f58-44ec-bde1-be902a8ca722" />

## 3. 典型对象定义 (POJO 示例)

### 3.1 DTO (接收参数)

```typescript
// AssetPageQueryDTO
{
  page: number;
  pageSize: number;
  name?: string;       // 房号模糊查询
  type?: string;       // SHOP, RESIDENTIAL
  status?: string;     // VACANT, SOLD等
}
```

### 3.2 VO (返回参数)

```typescript
// AssetTreeVO (递归结构)
{
  id: number;
  name: string;
  type: string;
  children: AssetTreeVO[];
}

// RepairKanbanVO (状态看板)
{
  pending: RepairVO[];    // 待处理
  processing: RepairVO[]; // 处理中
  completed: RepairVO[];  // 已办结
}
```

---

## 4. 开发注意事项

1. **路径占位符**: 使用 `{id}` 表示路径参数，Java 端需使用 `@PathVariable` 接收。
2. **分页封装**: `PageResult` 统一包含 `total` (总数) 和 `records` (当前页数据列表)。
3. **状态码规范**:
   * `200`: 成功。
   * `400`: 参数校验失败（前端传参有误）。
   * `401`: JWT 缺失或过期，前端需引导至登录页。
   * `403`: 角色权限不足。
   * `500`: 后端代码异常或数据库报错。
4. **Swagger/Knife4j**: 所有接口开发完成后，需及时在 Controller 增加 `@Operation` 注解，确保在线文档 `doc.html` 实时更新。
