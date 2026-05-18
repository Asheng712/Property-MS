# Docker 部署贡献说明

姓名：钱富安
学号：2312190501
日期：2026-05-18

## 我完成的工作

### 1. Dockerfile 编写

- [x] 前端 Dockerfile（多阶段构建）
- [x] 前端 `.dockerignore` 文件

本次主要负责前端容器化部署部分。新增 `frontend/Dockerfile`，使用 `node:22-alpine` 进行依赖安装和 Vite 构建，生产阶段使用 `nginxinc/nginx-unprivileged:1.27-alpine` 提供静态资源服务，满足多阶段构建和非 root 用户运行要求。

### 2. Compose 配置

- [x] 开发环境 `compose.yaml`
- [x] 生产环境 `compose.prod.yaml`
- [x] 前端健康检查配置

开发环境中，前端服务使用 Dockerfile 的 `dev` 阶段运行 Vite，并通过 volume 挂载支持热重载；生产环境中，前端服务暴露 `80:8080`，通过 `/health` 接口提供健康检查，并在 Nginx 中配置 `/api` 反向代理到后端服务。

### 3. 自动化部署

选择了选项 B：本地部署脚本 / Docker Compose 部署。

具体内容：

- 新增 `compose.yaml`，用于本地开发环境一键启动前端、后端和 MySQL。
- 新增 `compose.prod.yaml`，用于生产环境部署，包含健康检查、资源限制、数据库持久化和 Docker secrets 配置。
- 更新 `.env.example`，提供本地部署所需的环境变量模板。
- 更新 `.gitignore`，忽略本地 `.env` 和 `secrets/`，避免生产密码误提交。
- 调整 `frontend/vite.config.ts`，使开发代理读取 `VITE_API_URL`，便于容器环境中将请求转发到 `backend:8080`。

## PR 链接

- PR #X: https://github.com/Asheng712/Property-MS/pull/X

## 遇到的问题和解决

1. 问题：前端原本只能本地运行，缺少 Dockerfile 和 Nginx 生产入口。
   解决：新增前端多阶段 Dockerfile，构建阶段生成 `dist`，运行阶段使用非 root Nginx 镜像托管静态资源。
2. 问题：Vite 开发代理原先固定指向 `http://localhost:8080`，在 Docker Compose 网络中无法正确访问后端容器。
   解决：将代理目标改为读取 `VITE_API_URL`，开发 compose 中配置为 `http://backend:8080`。
3. 问题：生产环境需要避免硬编码数据库密码。
   解决：在 `compose.prod.yaml` 中使用 Docker secrets，并将 `secrets/` 加入 `.gitignore`。

## AI 使用情况

- AI 帮助解决的问题：
  - 生成前端多阶段构建方案和 Docker Compose 开发/生产配置。
  - 检查前端构建和 compose YAML 结构。

## 心得体会

本次 Docker 部署任务让我理解到前端项目在生产环境中不应该直接运行 Vite 开发服务器，而应先完成静态资源构建，再交给轻量级 Web 服务器托管。通过多阶段构建可以减少最终镜像体积，使用非 root 镜像和健康检查也能提升部署安全性与可维护性。

同时，开发环境和生产环境的配置目标不同：开发环境更关注热重载和调试效率，生产环境更关注稳定性、安全性、资源限制和密钥管理。将 `VITE_API_URL` 做成环境变量后，前端在本地、容器和生产环境之间切换会更加清晰。
