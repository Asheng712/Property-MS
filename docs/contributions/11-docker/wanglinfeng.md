# Docker 部署贡献说明（后端）

姓名：王林峰
学号：2312190519
日期：2026-05-19

## 我完成的工作

### 1. 后端 Dockerfile 多阶段构建

- [x] 后端 Dockerfile（多阶段构建：deps → dev → build → runtime）
- [x] 后端 `.dockerignore` 文件

基于 Maven 多模块项目的特点，设计了四阶段构建：

- **deps 阶段**：仅拷贝 `pom.xml`，利用 Docker 层缓存提前下载外部依赖，避免每次构建重复下载。
- **dev 阶段**：使用 `spring-boot:run` + Spring DevTools 启动，结合 volume 挂载源码实现热重载。
- **build 阶段**：编译打包为可执行 JAR。
- **runtime 阶段**：基于 `eclipse-temurin:17-jre-alpine` 精简镜像运行，非 root 用户启动，配置 G1GC 和容器感知 JVM 参数。

### 2. 国内镜像源适配

- [x] 新增 `backend/settings.xml`，配置阿里云 Maven 仓库镜像
- [x] Dockerfile 构建时将 `settings.xml` 注入容器 Maven 配置
- [x] 解决 Maven 多模块项目在容器中的构建顺序问题

由于项目为 Maven 多模块结构（`wisdom-pm` 父 POM → `wisdom-common` / `wisdom-pojo` / `wisdom-server`），`spring-boot:run` 启动前需先将父 POM 和依赖模块安装到本地仓库。通过 `mvn install -N` + `mvn install -pl` 分步执行解决。

### 3. Compose 配置与数据库

- [x] MySQL 容器配置（健康检查、数据持久化、初始化 SQL）
- [x] 开发环境变量管理（`.env`）
- [x] MySQL 8.4 用户权限兼容处理

MySQL 8.4 不允许 `MYSQL_USER=root` 与 root 账户冲突，通过调整 `.env` 中的 `DB_USERNAME=wisdom` 解决。数据库初始化 SQL 通过 `/docker-entrypoint-initdb.d` 自动执行。

### 4. 部署脚本增强

- [x] `deploy.sh` 增加 Docker 镜像加速器检测
- [x] 端口冲突处理（MySQL 3306 → 3307）
- [x] 生产环境密钥检查（`setup_secrets`）

## PR 链接

- PR #X: https://github.com/Asheng712/Property-MS/pull/X

## 遇到的问题和解决

1. 问题：`maven:3.9-eclipse-temurin-17-alpine` 基础镜像从 Docker Hub 拉取超时。
   解决：配置 Docker daemon `registry-mirrors` 使用国内镜像代理（1panel、daocloud、网易、腾讯云），并拉取成功。

2. 问题：`mvn dependency:go-offline` 只下载了外部依赖，`spring-boot:run -pl wisdom-server` 运行时无法解析同级模块 `wisdom-common` 和 `wisdom-pojo`。
   解决：CMD 改为三步执行：先 `install -N` 安装父 POM，再 `install -pl wisdom-common,wisdom-pojo` 安装依赖模块，最后 `spring-boot:run -pl wisdom-server` 启动。

3. 问题：MySQL 容器反复重启，日志报 `MYSQL_USER="root"` 不合法。
   解决：`.env` 中 `DB_USERNAME` 改为 `wisdom`（非 root 用户名），MySQL 8.4 镜像对 root 与普通用户做了严格区分。

4. 问题：MySQL 3306 端口与宿主机已有 MySQL 冲突。
   解决：`.env` 中设置 `MYSQL_PORT=3307`，将宿主机端口映射到 3307。

5. 问题：前端容器启动时 Rollup 报 `MODULE_NOT_FOUND: @rollup/rollup-linux-x64-musl`。
   解决：`package.json` 的 lock 文件在 Windows（glibc）环境生成，缺少 Alpine（musl）平台的 native 二进制。`npm install` 后显式安装同版本 `@rollup/rollup-linux-x64-musl`。

## AI 使用情况

- AI 帮助解决的问题：
  - 诊断 Docker Hub 被墙问题并配置国内镜像源加速。
  - 排查 Maven 多模块项目在容器中的构建依赖顺序问题。
  - 定位 MySQL 8.4 用户配置冲突和端口占用问题。
  - 解决前端 Rollup musl 平台二进制缺失问题。

## 心得体会

后端容器化与前端不同，主要挑战在于多模块 Maven 项目的构建依赖管理。Docker 的层缓存机制对加速构建很关键，但必须正确理解模块间的编译顺序——只缓存外部依赖是不够的，父 POM 和同级模块的本地安装也是必需的。

此外，国内网络环境对 Docker 部署的影响远超出预期：不仅是 Docker Hub 基础镜像，Maven 中央仓库和 npm 官方源都需要切换国内镜像。这次调试让我意识到在 CI/CD 流水线设计时就应当将镜像源、网络环境等因素考虑进去，而不是部署时才补救。
