# 物业管理系统 - 前端技术说明文档

## 1. 项目结构

系统前端分为两个独立项目：

| 项目 | 目录 | 技术栈 | 端口 | 用途 |
|------|------|--------|------|------|
| PC 管理端 | `frontend/` | Vue 3 + Vite + Element Plus + Pinia + Vue Router + Axios | 5173 | 物业管理员后台 |
| 移动端业主端 | `frontend-mobile/` | Vue 3 + Vite + Vant 4 + Pinia + Vue Router + Axios | 5174 | 业主自服务端 |

两个项目共享同一套后端 API (`localhost:8080/api/v1`)。

## 2. PC 管理端 (frontend/)

### 页面清单
- 登录/注册、管理员运营看板、资产全景管理、商业合同管理
- 智能账单引擎、缴费流水与核销、报修工单调度
- 投诉建议闭环、定向公告发布、角色权限控制、报表导出与导入、个人中心

### 组件库
Element Plus + @element-plus/icons-vue

### 布局
AdminLayout — 侧边栏 + 顶部导航 + 中央工作区，支持侧边栏折叠

## 3. 移动端业主端 (frontend-mobile/)

### 底部导航 (5 个 Tab)
首页 → 账单 → 服务 → 公告 → 我的

### 页面清单
- 认证：登录、注册
- 首页：轮播公告 + 快捷入口 + 待办事项
- 账单：待缴/已缴列表、在线缴费
- 报修：提交报修、报修列表、报修详情（进度跟踪）
- 投诉：提交投诉、投诉列表、投诉详情（处理结果）
- 公告：公告列表、公告详情
- 合同：合同列表、合同详情
- 个人中心：用户信息、编辑资料、退出登录

### 组件库
Vant 4 + unplugin-vue-components（按需自动导入）

### 特性
- keep-alive 缓存 Tab 页
- van-list 无限滚动 + van-pull-refresh 下拉刷新
- Axios 拦截器统一 Token 管理 + 401 自动跳转登录
- 数据隔离：账单/报修/投诉/合同均按当前登录用户自动过滤
- 报修创建：房屋从名下房产下拉选择（`GET /assets/my-houses`）
- 后端自动填 `reporterId`，前端无需传报修人/投诉人字段

## 4. 通用规范

- 函数式组件 (Composition API)
- TypeScript 严格类型，禁止 any
- Pinia setup store 模式管理状态
- 接口定义统一在 `src/services/api.ts`
- Token 存储在 sessionStorage，key: `wisdompm-token`
