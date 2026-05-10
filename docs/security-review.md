# 安全审查记录

日期：2026-05-10

## 审查范围

- 前端请求封装：`frontend/src/services/http.ts`
- 前端认证状态：`frontend/src/stores/app.ts`
- 前端入口鉴权失效处理：`frontend/src/main.ts`
- 前端依赖与 CI：`frontend/package-lock.json`、`.github/workflows/security.yml`

## AI 辅助审查发现

### 1. 认证 token 持久化存储

- 类型：敏感信息暴露 / XSS 后扩大影响
- 危害等级：中
- 问题：认证 token 原先存入 `localStorage`，一旦页面出现 XSS，攻击者可以长期读取 token，浏览器重启后仍然有效。
- 修复：改为 `sessionStorage` 保存 token，并在写入新 token 时清理旧的 `localStorage` 同名键。
- 涉及文件：`frontend/src/services/http.ts`

### 2. 用户资料和登录态持久化存储

- 类型：敏感信息暴露 / 会话生命周期过长
- 危害等级：中
- 问题：用户资料、登录态和偏好原先存入 `localStorage`，包含手机号、邮箱、角色等展示信息，不适合长期保留。
- 修复：改为 `sessionStorage` 保存当前会话状态；登出或资料为空时同步清理历史 `localStorage` 数据。
- 涉及文件：`frontend/src/stores/app.ts`

### 3. Token 失效后未统一清理前端状态

- 类型：失效的认证状态处理
- 危害等级：中
- 问题：后端返回 HTTP 401/403 或业务码 401/403 时，请求层只抛错，没有统一清理 token，也不会引导用户重新登录。
- 修复：请求层统一触发 `wisdompm:auth-expired` 事件，入口文件监听后执行登出并跳转登录页。
- 涉及文件：`frontend/src/services/http.ts`、`frontend/src/main.ts`

### 4. 前端依赖存在高危漏洞

- 类型：依赖安全
- 危害等级：高
- 问题：`npm audit --audit-level=high` 发现 Vite、lodash、lodash-es、picomatch 等依赖链存在高危漏洞。
- 修复：执行 `npm audit fix` 更新锁文件，复测 `npm audit --audit-level=high` 为 0 vulnerabilities。
- 涉及文件：`frontend/package-lock.json`

## 安全检查清单

### 认证与授权

- [x] 密码存储：前端不存储密码；登录/注册只在请求体中提交，密码哈希应由后端负责。
- [x] JWT / Session：前端 token 改为会话级 `sessionStorage`，收到 401/403 后清理并跳转登录页。
- [x] 接口鉴权：前端请求封装统一附加 `Authorization: Bearer <token>`。
- [ ] 越权访问：前端无法单独保证，需要后端按当前登录用户和角色校验资源权限。

### 注入防护

- [x] SQL：前端不直接执行 SQL，不适用。
- [x] XSS：未发现 `v-html`、`innerHTML`、`eval` 等危险渲染；Vue 模板默认转义用户数据。

### 敏感信息

- [x] API Key / 密码：未发现前端硬编码 API Key 或密码；新增 `.env.example` 作为配置模板。
- [x] `.env` 文件：已加入 `.gitignore`，仓库仅提交 `.env.example`。

### 依赖安全

- [x] 运行依赖扫描：`npm audit --audit-level=high` 已通过，当前 0 vulnerabilities。

## CI 安全扫描

- 配置文件：`.github/workflows/security.yml`
- 扫描项：
  - Gitleaks 密钥泄露扫描
  - 前端 `npm audit --audit-level=high` 依赖漏洞扫描

## 验证结果

- `npm audit --audit-level=high`：通过，0 vulnerabilities
- `npm run lint`：通过
- `npm test`：通过，20 tests passed
