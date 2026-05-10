# 安全审查贡献说明

姓名：钱富安
学号：2312190501
日期：2026-05-10

## 我完成的工作

### AI 安全审查

- 审查了哪些文件/模块：
  - `frontend/src/services/http.ts`
  - `frontend/src/stores/app.ts`
  - `frontend/src/main.ts`
  - `frontend/package-lock.json`
  - `.github/workflows/security.yml`
- AI 发现的主要问题：
  - token 使用 `localStorage` 持久化保存，XSS 后影响时间过长。
  - 用户资料和登录态使用 `localStorage` 长期保存，包含手机号、邮箱、角色等信息。
  - 401/403 鉴权失败后前端没有统一清理认证状态。
  - 前端依赖扫描发现高危漏洞。
- 我修复了哪些问题：
  - 将 token 和用户会话状态迁移到 `sessionStorage`。
  - 统一处理 401/403，清理 token 并跳转登录页。
  - 执行 `npm audit fix` 消除前端高危依赖漏洞。
  - 增加 GitHub Actions 安全扫描工作流。

### 安全检查清单

- [x] 密码存储：前端不保存密码；后端负责 bcrypt / argon2 等哈希处理。
- [x] JWT / Session：前端 token 使用会话存储，401/403 后清理并跳转登录页。
- [x] 接口鉴权：请求封装统一携带 Authorization header。
- [x] 越权访问：需要后端继续基于用户身份和角色校验资源权限。
- [x] SQL：前端不直接执行 SQL，不适用。
- [x] XSS：未使用 `v-html`、`innerHTML`、`eval` 等危险渲染方式。
- [x] API Key / 密码：未发现前端硬编码密钥；新增 `.env.example`。
- [x] `.env` 文件：已加入 `.gitignore`。
- [x] 依赖安全：`npm audit --audit-level=high` 已通过。

### CI 安全扫描

- 配置了哪个选项（A/B/C）：A + B
- 扫描结果：本地 `npm audit --audit-level=high` 通过，GitHub Actions 将在 push / pull_request 时运行 Gitleaks 与 npm audit。

### 选做完成情况

- 统一鉴权失败处理：401/403 自动清理前端认证状态并跳转登录页。

## 遇到的问题和解决

1. 问题：前端实际技术栈为 Vue 3 + Vite，与项目规则文档中 React 描述不一致。
   解决：按当前仓库真实技术栈和现有代码风格完成安全加固，避免引入框架级改动。
2. 问题：依赖扫描发现高危漏洞，会导致 CI 安全扫描失败。
   解决：执行 `npm audit fix` 更新锁文件，并重新运行审计、lint、测试。

## 心得体会

Vibe Coding 可以快速发现显眼的安全问题，但修复时仍要结合项目现状判断影响范围。前端无法替代后端权限校验，不过可以减少敏感数据暴露、统一处理失效会话，并把依赖扫描放进 CI，让安全问题更早暴露。
