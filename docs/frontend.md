# 物业管理系统 — 前端技术说明文档

## 1. 项目结构

| 项目 | 目录 | 技术栈 | 端口 | 用途 |
|------|------|--------|------|------|
| PC 管理端 | `frontend/` | Vue 3 + Vite + Element Plus + Pinia + Vue Router | 5173 | 物业管理员后台 |
| 移动端业主端 | `frontend-mobile/` | Vue 3 + Vite + Vant 4 + Pinia + Vue Router | 5174 | 业主自服务端 |

两个项目共享后端 API (`localhost:8080/api/v1`)，通过 Nginx 反向代理或 Vite proxy 转发。当前版本: **v2.1.2**。

## 2. PC 管理端 (`frontend/`)

### 页面清单
- 登录/注册、管理员运营看板（待收费/欠费/投诉/报修/资产租售占比）
- 资产管理（楼栋/单元/商铺/住宅 CRUD + 空间树）
- 商业合同管理 + 资产申请审批（购买/租赁）
- 账单管理（按费用类型生成 + 列表查询 + 状态筛选）
- 缴费流水与核销（查看支付记录 + 核销 + 撤销核销）
- 报修工单调度（看板 + 派发 + 状态更新）
- 投诉建议处理、公告发布、角色权限、报表导入导出、个人中心

### 技术要点
- HTTP 层改用原生 `fetch`（替代 Axios），含请求监控与性能打点
- ESLint + vue-tsc 严格类型检查，禁止 `any`
- 组件库: Element Plus + @element-plus/icons-vue
- 布局: AdminLayout（侧边栏 + 顶部导航，md 以下自动折叠）

## 3. 移动端业主端 (`frontend-mobile/`)

### 底部导航 (5 Tab)
首页 → 账单 → 服务 → 公告 → 我的

### 页面清单
- 认证：登录、注册
- 首页：轮播公告 + 快捷入口 + 待办事项（待缴费数量实时从 API 获取）
- 账单：待缴/已缴列表、在线缴费（支持合并缴费）
- 报修：提交报修（名下房屋下拉选择）、报修列表、报修详情
- 投诉：提交投诉、投诉列表
- 公告：公告列表、公告详情
- 合同：合同列表（按当前用户过滤）
- 资产：资产列表、资产详情、购买/租赁申请
- 个人中心：用户信息、编辑资料、关于我们（v2.1.2）

### 技术要点
- keep-alive 缓存 Tab 页
- van-list 无限滚动 + van-pull-refresh 下拉刷新
- Pinia setup store 模式管理状态
- 首页待办事项直接调 API 取总数，不受分页限制
- 报修房屋选择使用 Vant Picker 标准 `{ text, value }` 格式
- Token 存储在 sessionStorage，key: `wisdompm-mobile-token`

## 4. 通用规范

- 函数式组件 (Composition API)
- TypeScript 严格类型
- 接口定义统一在 `src/services/api.ts`
- 数据隔离: 账单/报修/投诉/合同均按当前登录用户自动过滤
- 报修/投诉: 后端自动填 `reporterId`，前端无需传报修人/投诉人字段

## 5. 开发环境

```bash
# PC 管理端
cd frontend && npm install && npm run dev

# 移动端
cd frontend-mobile && npm install && npm run dev

# 或 Docker 一键启动全部服务
cp .env.example .env
docker compose up -d --build
```

---

**更新时间**: 2026-06-08 — v2.1.2
