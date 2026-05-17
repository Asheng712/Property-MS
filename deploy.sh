#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

# ---- 检查依赖 ----
command -v docker >/dev/null 2>&1 || { echo "[ERROR] Docker 未安装，请先安装 Docker Desktop"; exit 1; }

# ---- .env 文件 ----
if [ ! -f .env ]; then
    echo "[INFO] .env 不存在，从 .env.example 复制"
    cp .env.example .env
    echo "[WARN] 请编辑 .env 填入实际的数据库连接信息，再重新运行"
    exit 1
fi

echo "========================================="
echo "  智慧物业管理系统 — Docker 部署"
echo "========================================="

# ---- 构建 ----
echo ""
echo "[1/2] 构建后端镜像..."
docker compose build --no-cache

# ---- 启动 ----
echo ""
echo "[2/2] 启动容器..."
docker compose up -d

echo ""
echo "========================================="
echo "  部署完成"
echo "  API 文档: http://localhost:8080/doc.html"
echo "  查看日志: docker compose logs -f backend"
echo "  停止服务: docker compose down"
echo "========================================="
