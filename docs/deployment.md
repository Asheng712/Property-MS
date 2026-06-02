# 云服务部署说明

## 概述

本文档描述智慧物业管理系统(WisdomPM)的云服务部署方案，涵盖**前端**（Vue 3 + Vite）的 Vercel 部署和**后端**的 Docker 部署。

---

## 一、平台选择

| 服务   | 平台       | 说明                     |
| ------ | ---------- | ------------------------ |
| 前端   | Vercel     | 免费静态托管 + 自动部署  |
| 前端   | Docker     | 自托管 Nginx 镜像        |
| 后端   | Docker     | Railway / Render 部署    |

---

## 二、前端部署

### 方式一：Vercel（推荐）

Vercel 原生支持 Vite 项目，配置简单、自动 HTTPS、全球 CDN。

#### 配置文件

`frontend/vercel.json`:

```json
{
  "buildCommand": "npm run build",
  "outputDirectory": "dist",
  "devCommand": "npm run dev -- --host 0.0.0.0",
  "framework": "vite",
  "rewrites": [
    { "source": "/((?!api/).*)", "destination": "/index.html" }
  ]
}
```

- `rewrites` 配置 SPA 路由回退，所有非 `/api/` 请求指向 `index.html`
- 生产环境 `VITE_API_URL` 需在 Vercel Settings → Environment Variables 中配置

#### 部署步骤

1. 推送代码至 GitHub 仓库
2. 在 [Vercel](https://vercel.com) 注册并连接 GitHub
3. 导入仓库，选择 `frontend` 目录作为 Root Directory
4. Vercel 自动检测 Vite 框架，应用 `vercel.json` 配置
5. 配置环境变量 `VITE_API_URL`（指向后端 API 地址）
6. 部署完成，获得 `https://xxx.vercel.app` 访问地址

#### 自动部署

Vercel 默认启用 Git 自动部署：
- 推送到 `main` 分支 → 自动触发生产部署
- 创建 PR → 自动生成预览环境

### 方式二：Docker

使用已有的 `frontend/Dockerfile` 构建 Nginx 静态镜像。

#### 构建与运行

```bash
cd frontend
docker build -t wisdompm-frontend .
docker run -d -p 8080:8080 wisdompm-frontend
```

#### Docker 多阶段构建

- **deps**: 安装 Node.js 依赖
- **build**: 执行 `npm run build` 生成 `dist/`
- **runtime**: Nginx 提供静态文件服务，暴露 8080 端口

#### Nginx 配置要点

- SPA 路由支持：`try_files $uri $uri/ /index.html`
- API 代理：`/api/` → `http://backend:8080`
- 健康检查：`/health` 返回 200

---

## 三、环境变量配置

### 前端环境变量

| 变量           | 说明              | 开发值                  | 生产值                            |
| -------------- | ----------------- | ----------------------- | --------------------------------- |
| `VITE_API_URL` | 后端 API 基地址   | `http://localhost:8080` | `https://backend.onrender.com`    |

**Vercel 配置方式**: Settings → Environment Variables → 添加 Key/Value

**Docker 配置方式**: `docker run -e VITE_API_URL=https://api.example.com ...`

注意：Vite 的 `VITE_` 前缀变量会在构建时注入，运行时无法变更。如需运行时配置，应在 Nginx 层处理。

---

## 四、自动部署流程

```
Git Push (main) → Vercel 检测变更 → npm run build → 部署 dist/ → 生效
```

1. 代码推送至 `origin/main`
2. Vercel 通过 Webhook 感知变更
3. 执行 `npm run build`（含 `vue-tsc` 类型检查 + `vite build`）
4. 构建产物 `dist/` 部署至 CDN
5. 自动 HTTPS 证书续期

---

## 五、文件清单

| 文件                              | 说明                     |
| --------------------------------- | ------------------------ |
| `frontend/vercel.json`            | Vercel 部署配置          |
| `frontend/.env.production`        | 生产环境变量模板         |
| `frontend/.env.example`           | 本地环境变量模板         |
| `frontend/Dockerfile`             | Docker 多阶段构建       |
| `frontend/nginx.conf`             | Nginx 配置文件          |
| `docs/deployment.md`              | 本文档                  |
