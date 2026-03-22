# 智慧物业管理系统 (WisdomPM) AI辅助开发规则文档

## 1. 项目架构规范 (Project Architecture)

项目采用前后端分离架构，后端基于 Maven 多模块构建，前端基于 React + Vite。

### 1.1 后端模块职责 (wisdom-pm)
- **`wisdom-common`**: 存放工具类、常量、异常、枚举、结果封装等。该模块不含业务逻辑。
- **`wisdom-pojo`**: 存放所有数据对象。严禁在此模块编写业务逻辑。
- **`wisdom-server`**: 核心业务模块。包含配置文件、Controller、Service、Mapper 等，是唯一可运行的模块。

### 1.2 前端模块职责 (property-web)
- **`src/components`**: 原子化 UI 组件（如按钮、输入框、弹窗卡片）。
- **`src/pages`**: 业务页面逻辑。
- **`src/services`**: 统一管理 Axios 接口调用。
- **`src/types`**: 定义与后端 POJO 对应的 TypeScript 接口。

---

## 2. 命名与代码规范 (Naming & Coding)

### 2.1 命名约定
- **数据库表名/字段**: 小写字母 + 下划线（如 `bus_house`, `create_time`）。
- **Java 类名**: 大驼峰（如 `HouseController`）。
- **Java 变量/方法**: 小驼峰（如 `getHouseInfo`）。
- **TS 文件名**: 大驼峰或小写横杠（如 `HouseList.tsx` 或 `house-list.tsx`）。
- **Tailwind 类名**: 语义化组合，禁止使用无意义的自定义类名。

### 2.2 开发禁止事项 (Strictly Forbidden)
- **禁止使用 `any` 类型**: 在 TS 中必须明确定义接口。
- **禁止内联样式**: 所有样式必须通过 Tailwind CSS 实现。
- **禁止直接操作 DOM**: 必须通过 React 状态 (`state`) 管理。
- **禁止修改核心配置文件**: 除非功能需要，不得修改 `application.yml` 或 `vite.config.ts`。

---

## 3. 数据流转规范 (Data Flow)

为了保证代码整洁，严格遵守以下对象使用规范：

| 对象类型 | 全称 | 用途 | 存放位置 |
| :--- | :--- | :--- | :--- |
| **Entity** | 实体类 | 与数据库表结构一一对应，仅限 Mapper 层使用 | `wisdom-pojo.entity` |
| **DTO** | 数据传输对象 | 接收前端 `POST/PUT` 请求参数 | `wisdom-pojo.dto` |
| **VO** | 视图对象 | 返回给前端用于展示的数据（脱敏、组合） | `wisdom-pojo.vo` |
| **Result** | 结果封装 | 统一的 API 响应载体 | `wisdom-common.result` |

---

## 4. 后端开发准则 (Backend Rules)

### 4.1 统一 API 响应
所有 Controller 必须返回 `Result<T>`。
```java
@GetMapping("/{id}")
public Result<HouseVO> getById(@PathVariable Long id) {
    HouseVO houseVO = houseService.getById(id);
    return Result.success(houseVO);
}
```

### 4.2 异常处理
- 业务逻辑失败时，通过 `throw new BaseException("错误信息")` 抛出。
- 由 `wisdom-server.handler.GlobalExceptionHandler` 统一捕获并返回给前端。

### 4.3 自动填充 (AOP)
- 使用自定义注解 `@AutoFill` 配合切面，在 `insert` 和 `update` 时自动填充 `create_time`, `update_time`, `create_user`, `update_user`。

---

## 5. 前端开发准则 (Frontend Rules)

### 5.1 组件开发
- 必须使用 **函数式组件 (Functional Components)**。
- 复杂逻辑必须封装在自定义 **Hooks** 中。

### 5.2 状态管理与请求
- 接口定义必须在 `src/services` 中。
- Axios 响应拦截器需统一处理 `code !== 200` 的情况（如 Token 过期自动跳转登录）。

### 5.3 响应式适配
- 布局必须基于 Tailwind 的断点（`sm`, `md`, `lg`）。
- 侧边栏在 `md` 以下屏幕应自动折叠或隐藏。

---

## 6. 数据库规范 (Database)

- **主键**: 统一使用 `BIGINT` 类型自增。
- **金额**: 必须使用 `DECIMAL(12,2)`，严禁使用 `float/double`。
- **状态**: 使用 `TINYINT` 表示（如 0-禁用，1-启用），并在 `wisdom-common.enumeration` 中定义对应枚举。
- **审计字段**: 每张表必须包含 `create_time` 和 `update_time`。

---

## 7. 协作与部署 (Workflow)

- **API 文档**: 后端启动后访问 `http://localhost:8080/doc.html` (Knife4j)，前端根据文档定义 `types`。
- **上下文管理**: 后端使用 `ThreadLocal` (即 `wisdom-common` 中的 `BaseContext`) 存储当前登录用户 ID。
- **部署**: 提供 `docker-compose.yml`，一键拉起 MySQL、后端服务和 Nginx 前端镜像。

---

**后续开发中，请确保 `wisdom-pojo` 的变动及时同步给前端，并严格按照 DTO -> Service -> Entity 的流程操作数据库。**
