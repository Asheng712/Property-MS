# 部署说明

## 概述

智慧物业管理系统 (WisdomPM) 支持 **Docker Compose 一键部署**（开发/生产），以及 **Vercel/Docker** 单独部署前端。

---

## 一、Docker Compose 部署（推荐）

### 开发环境

```bash
cp .env.example .env         # 编辑数据库密码和 JWT 密钥
docker compose up -d --build  # 启动全部服务
```

启动后访问:
- PC 管理端: `http://localhost:5173`
- 移动端: `http://localhost:5174`
- 后端 API: `http://localhost:8080`
- API 文档: `http://localhost:8080/doc.html`

### 生产环境

```bash
cp .env.secrets.example .env.secrets   # 编辑密钥
mkdir -p secrets
echo "your-password" > secrets/db_password.txt
echo "your-root-password" > secrets/mysql_root_password.txt
docker compose -f compose.prod.yaml up -d --build
```

### 服务架构

| 服务 | 镜像 | 端口 |
|------|------|------|
| frontend | Nginx (Vite 构建产物) | 80 |
| mobile | Vite dev server | 5174 |
| backend | Spring Boot (Maven) | 8080 |
| db | MySQL 8.4 | 3306 (内部) |

后端通过环境变量注入数据库连接和 JWT 密钥。数据库初始化脚本位于 `sql/` 目录，容器首次启动时自动执行。

---

## 二、前端单独部署

### PC 管理端

**Vercel**:
1. 导入 GitHub 仓库，Root Directory 设为 `frontend`
2. 配置环境变量 `VITE_API_URL` 指向后端地址
3. 自动部署

**Docker**:
```bash
cd frontend
docker build -t wisdompm-frontend .
docker run -d -p 8080:8080 wisdompm-frontend
```

### 移动端

```bash
cd frontend-mobile
docker build -t wisdompm-mobile .
docker run -d -p 5174:5174 wisdompm-mobile
```

---

## 三、CI/CD 流水线

| 工作流 | 触发条件 | 内容 |
|--------|----------|------|
| `ci.yml` | Push/PR to main/develop | 后端测试(89个) + 前端 lint+test |
| `security.yml` | Push/PR to main | Gitleaks 密钥扫描 + npm audit |
| `deploy.yml` | Push to main / tag v* | CI 通过后 SSH 部署到生产服务器 |

CI 失败时自动发送邮件通知（需配置 `MAIL_USERNAME` / `MAIL_PASSWORD` Secrets）。

---

## 四、文件清单

| 文件 | 说明 |
|------|------|
| `compose.yaml` | 开发环境 Docker Compose |
| `compose.prod.yaml` | 生产环境 Docker Compose |
| `.env.example` | 开发环境变量模板 |
| `.env.secrets.example` | 生产环境变量模板 |
| `sql/01-init.sql` | 数据库初始化 DDL + 种子数据 |
| `backend/Dockerfile` | 后端多阶段构建 (dev/build/runtime) |
| `frontend/Dockerfile` | 前端 Nginx 镜像 |
| `frontend-mobile/Dockerfile` | 移动端 Vite 镜像 |
| `.github/workflows/` | CI/CD 配置 |
