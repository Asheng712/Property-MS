# 前端开发贡献说明

姓名：钱富安
学号：2312190501
技术栈：Vue 3 / Vite / TypeScript / Element Plus / Pinia / Vue Router
日期：2026-04-26

## 我完成的工作

### 页面开发

- [x] 登录/注册页面
- [x] 首页/列表页面
- [x] 详情页面：`frontend/src/views/assets/AssetDetailView.vue`
- [x] 个人中心：`frontend/src/views/profile/ProfileView.vue`
- [x] 其他：资产管理、商业合同、智能账单、缴费流水、报修、投诉、公告、角色权限、报表导入导出

### 组件/模块封装

- 组件1：`PageContainer`，统一业务页面标题、描述和操作区布局。
- 组件2：`PanelCard`，统一卡片区域标题、描述、操作插槽和内容承载。
- 组件3：`DataToolbar`，封装搜索输入、状态筛选和操作按钮区域。
- 组件4：`InfoList`，用于详情页和个人中心的键值信息展示。
- 组件5：`StatusBadge` / `StatCard` / `PieLegendCard`，用于状态、指标和图表摘要展示。

### API 对接

- [x] 封装网络请求层：`frontend/src/services/http.ts`
- [x] 对接后端接口：登录、注册、用户信息、资产、合同、账单、缴费、报修、投诉、公告、角色、导入导出
- [x] 处理加载状态和错误：页面使用 `v-loading`、`ElMessage`、`ElAlert` 展示加载与异常反馈
- [x] Mock 联调：`frontend/scripts/mock-backend.mjs` 可模拟后端接口，支持页面运行截图

### 页面截图

- `docs/design/screenshots/01-login.png`
- `docs/design/screenshots/02-dashboard.png`
- `docs/design/screenshots/03-assets.png`
- `docs/design/screenshots/04-asset-detail.png`
- `docs/design/screenshots/05-profile.png`
- `docs/design/screenshots/06-repairs.png`

## PR 链接

- 暂无

## 遇到的问题和解决

1. 问题：详情页需要独立路由，但后端暂未提供单条资产详情接口。
   解决：先通过资产分页接口读取台账数据，并在前端按 `id` 定位记录，后续可平滑替换为 `GET /api/v1/assets/{id}`。

2. 问题：页面截图需要后端接口数据支撑。
   解决：使用本地 mock 后端提供与真实接口一致的响应结构，保证前端页面可以独立运行和验证。

## 心得体会

本次前端开发重点是把页面、组件和接口封装串成完整闭环。通过统一请求层、复用组件和 Pinia 状态管理，页面代码更容易维护；新增详情页和个人中心后，系统的信息层级也更完整。
