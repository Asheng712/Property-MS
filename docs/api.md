# 智慧物业管理系统 (WisdomPM) API 接口设计文档

## 1. 通用说明

### 1.1 基础信息
- **网关地址**: `http://localhost:8080/api/v1`
- **数据格式**: `Content-Type: application/json`
- **认证方式**: `Authorization: Bearer <JWT_TOKEN>`
- **接口文档**: 启动后端后访问 `http://localhost:8080/doc.html` (Knife4j)

### 1.2 统一返回格式 (`Result<T>`)
```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```
状态码: 200-成功, 400-参数错误, 401-未登录, 403-无权限, 404-未找到, 500-系统异常

---

## 2. 模块 API 设计

### 2.1 认证与权限 (Auth)

| 功能 | 方法 | 路径 | 说明 |
|:---|:---|:---|:---|
| 用户登录 | `POST` | `/auth/login` | 返回 JWT Token |
| 用户注册 | `POST` | `/auth/register` | - |
| 获取当前用户信息 | `GET` | `/auth/info` | 需登录 |
| 更新个人资料 | `PUT` | `/auth/profile` | 需登录 |

### 2.2 资产与合同 (Asset & Contract)

| 功能 | 方法 | 路径 | 说明 |
|:---|:---|:---|:---|
| 获取资产空间树 | `GET` | `/assets/tree` | 楼栋→单元→房间层级 |
| 获取名下房屋 | `GET` | `/assets/my-houses` | 当前用户拥有的房屋列表 |
| 查询资产详情 | `GET` | `/assets/{id}` | - |
| 分页查询资产 | `GET` | `/assets` | 支持类型/状态/名称筛选 |
| 新增/编辑资产 | `POST/PUT` | `/assets` | - |
| 删除资产 | `DELETE` | `/assets/{id}` | - |
| 分页查询合同 | `GET` | `/contracts` | 非admin自动过滤名下房屋 |
| 新增合同 | `POST` | `/contracts` | **自动生成当月账单** |
| 终止合同 | `PUT` | `/contracts` | 释放资产为空置 |

### 2.3 资产申请审批 (Purchase Application)

| 功能 | 方法 | 路径 | 说明 |
|:---|:---|:---|:---|
| 分页查询申请 | `GET` | `/purchase-applications` | 支持类型/状态筛选 |
| 查询申请详情 | `GET` | `/purchase-applications/{id}` | - |
| 提交购买/租赁申请 | `POST` | `/purchase-applications` | 移动端用户申请 |
| 审批申请 | `PUT` | `/purchase-applications/approve` | **通过后自动创建合同+生成账单** |

### 2.4 财务与缴费 (Finance)

| 功能 | 方法 | 路径 | 说明 |
|:---|:---|:---|:---|
| 生成账单 | `POST` | `/bills/generate` | 按费用类型+月份+房屋生成 |
| 分页查询账单 | `GET` | `/bills` | 非admin自动过滤本人账单 |
| 提交缴费 | `POST` | `/payments` | 支持多账单合并支付 |
| 分页查询支付记录 | `GET` | `/payments` | - |
| 支付记录详情 | `GET` | `/payments/{id}` | 含关联账单明细 |
| 核销支付 | `PUT` | `/payments/{id}/verify` | 管理员操作，账单→已缴费 |
| 撤销核销 | `PUT` | `/payments/{id}/cancel` | 管理员操作，账单→待缴费 |
| 设置物业费单价 | `POST` | `/property-fee-config` | 下月生效 |
| 查询当前单价 | `GET` | `/property-fee-config` | - |

#### 缴费状态流转
```
用户提交缴费 → 支付记录:待核销(0), 账单:待核销(1)
管理员核销   → 支付记录:已核销(1), 账单:已缴费(2)
管理员撤销   → 支付记录:已撤销(3), 账单:待缴费(0)
```

### 2.5 服务模块 (Repair & Complaint)

| 功能 | 方法 | 路径 | 说明 |
|:---|:---|:---|:---|
| 获取报修看板 | `GET` | `/repairs/kanban` | 待处理/处理中/已办结分组 |
| 提交报修 | `POST` | `/repairs` | 仅可报修名下房屋 |
| 派发工单 | `PUT` | `/repairs/dispatch` | 指派师傅 |
| 更新工单状态 | `PUT` | `/repairs/status` | - |
| 分页查询投诉 | `GET` | `/complaints` | - |
| 提交投诉 | `POST` | `/complaints` | reporterId 后端自动填 |
| 处理投诉 | `PUT` | `/complaints/handle` | - |

### 2.6 公告与系统 (Notice & System)

| 功能 | 方法 | 路径 | 说明 |
|:---|:---|:---|:---|
| 分页查询公告 | `GET` | `/notices` | - |
| 发布/暂存公告 | `POST` | `/notices` | - |
| 获取导入导出任务 | `GET` | `/system/tasks` | - |
| 导出财务报表 | `GET` | `/system/export/finance` | - |
| 导入资产数据 | `POST` | `/system/import/assets` | MultipartFile |

### 2.7 运营看板 (Dashboard)

| 功能 | 方法 | 路径 | 说明 |
|:---|:---|:---|:---|
| 获取看板数据 | `GET` | `/dashboard/data` | 待收费/欠费/投诉/报修/资产租售占比 |

---

## 3. 典型 DTO 示例

```typescript
// BillGenerateDTO
{ feeType: number; billMonth: string; houseId?: number; remark?: string }

// PaymentSubmitDTO (多账单合并支付)
{ billIds: number[]; payMethod: number; voucherUrl?: string; remark?: string }

// PaymentCancelDTO (撤销核销)
{ id: number; cancelReason: string }

// PurchaseApprovalDTO (审批)
{ id: number; approved: boolean; proposedPrice?: number; startDate?: string; endDate?: string; deposit?: number }
```

## 4. 数据隔离规则
非管理员用户查询账单/缴费/报修/投诉/合同时，后端自动按 `userId` 过滤，仅返回本人名下数据。报修创建时校验房屋归属 (`owner_id`)。
