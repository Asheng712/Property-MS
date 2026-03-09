# 物业管理系统 - API 接口设计文档

## 1. 通用说明

*   **Base URL:** `http://localhost:8080/api/v1`
*   **请求格式:** `Content-Type: application/json`
*   **认证方式:** Header 携带 `Authorization: Bearer <JWT_TOKEN>`
*   **状态码定义:**
    *   `200`: 请求成功
    *   `401`: 未登录或 Token 过期
    *   `403`: 无权限访问
    *   `500`: 服务器内部错误

---

## 2. 基础响应格式

所有接口均返回以下统一结构：
```json
{
  "code": 200,      // 业务状态码
  "msg": "操作成功", // 提示信息
  "data": {}        // 具体业务数据（对象或数组）
}
```

---

## 3. 核心 API 接口列表

### 3.1 用户认证模块 (Auth)
| 接口名称 | 请求方法 | 路径 | 说明 |
| :--- | :--- | :--- | :--- |
| 用户登录 | `POST` | `/auth/login` | 提交用户名密码，返回 JWT Token |
| 获取当前用户信息 | `GET` | `/auth/info` | 返回当前登录用户的角色、权限、个人信息 |
| 退出登录 | `POST` | `/auth/logout` | 注销当前 Token |

### 3.2 房产管理模块 (House)
| 接口名称 | 请求方法 | 路径 | 参数 (Query/Body) |
| :--- | :--- | :--- | :--- |
| 分页查询房屋 | `GET` | `/houses` | `page`, `size`, `buildingNum` (可选) |
| 添加房屋信息 | `POST` | `/houses` | `buildingNum`, `unitNum`, `roomNum`, `area` |
| 修改房屋信息 | `PUT` | `/houses/{id}` | 房屋对象 JSON |
| 删除房屋 | `DELETE` | `/houses/{id}` | 房屋 ID |

### 3.3 缴费管理模块 (Bill)
| 接口名称 | 请求方法 | 路径 | 说明 |
| :--- | :--- | :--- | :--- |
| 查询账单列表 | `GET` | `/bills` | 支持按状态（已缴/未缴）和业主名筛选 |
| 生成月度账单 | `POST` | `/bills/generate` | 管理员触发，根据单价和面积生成本月账单 |
| 模拟在线缴费 | `PUT` | `/bills/pay/{id}` | 住户操作，将账单状态改为“已缴” |
| 导出欠费报表 | `GET` | `/bills/export` | 返回 Excel 文件流（使用 EasyExcel 实现） |

### 3.4 报修管理模块 (Repair)
| 接口名称 | 请求方法 | 路径 | 参数说明 |
| :--- | :--- | :--- | :--- |
| 提交报修申请 | `POST` | `/repairs` | `description`, `images` (住户端) |
| 获取报修列表 | `GET` | `/repairs` | 住户看个人，管理员看全部 |
| 更新报修状态 | `PUT` | `/repairs/{id}/status` | `status` (处理中/已完成) |

### 3.5 公告管理模块 (Notice)
| 接口名称 | 请求方法 | 路径 | 说明 |
| :--- | :--- | :--- | :--- |
| 发布公告 | `POST` | `/notices` | 管理员发布（标题、内容） |
| 获取最新公告 | `GET` | `/notices/latest` | 住户端展示最新的 5 条公告 |

---

## 4. 典型接口数据示例

### 接口：`GET /api/v1/houses` (获取房屋列表)

**请求参数:**
*   `page`: 1
*   `size`: 10

**响应数据:**
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "records": [
      {
        "id": 101,
        "buildingNum": "12号楼",
        "unitNum": "1单元",
        "roomNum": "502",
        "area": 89.5,
        "ownerName": "张三",
        "ownerPhone": "13800138000"
      }
    ],
    "total": 50,
    "current": 1
  }
}
```

---

## 5. 开发建议
1.  **接口测试:** 建议在后端集成 **Knife4j** (Swagger)，启动项目后访问 `http://localhost:8080/doc.html` 即可直接在线调试上述所有接口。
2.  **前端调用:** 前端 Axios 请求拦截器中需统一处理 `401` 错误，若发现 Token 过期，应强制跳转回登录页。
3.  **安全性:** 对于 `DELETE` 或 `POST` 类敏感接口，后端 Controller 需加 `@PreAuthorize("hasRole('ADMIN')")` 权限注解。

