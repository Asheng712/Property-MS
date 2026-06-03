#!/usr/bin/env bash
# ============================================
# 智慧物业管理系统 — 本地部署脚本
# 用法:
#   ./deploy.sh dev          开发环境（热重载）
#   ./deploy.sh prod         生产环境
#   ./deploy.sh stop         停止所有服务
#   ./deploy.sh restart      重启
#   ./deploy.sh logs [svc]   查看日志
#   ./deploy.sh status       服务状态
#   ./deploy.sh backup       备份数据库
#   ./deploy.sh clean        清理（容器+卷+镜像）
# ============================================
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log()  { echo -e "${GREEN}[INFO]${NC}  $*"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $*"; }
err()  { echo -e "${RED}[ERROR]${NC} $*" >&2; }

check_deps() {
    command -v docker >/dev/null 2>&1 || { err "Docker 未安装"; exit 1; }
    docker compose version >/dev/null 2>&1  || { err "Docker Compose 未安装"; exit 1; }

    # 检查 Docker 镜像加速器（国内网络必需）
    if docker info 2>/dev/null | grep -q "Registry Mirrors"; then
        log "Docker 镜像加速器已配置"
    else
        warn "未检测到 Docker 镜像加速器（Registry Mirrors）"
        warn "国内网络可能无法从 Docker Hub 拉取镜像"
        warn "请配置: /etc/docker/daemon.json → registry-mirrors"
        echo ""
        echo "  推荐镜像加速器:"
        echo "    1. 阿里云 (需注册获取专属加速地址): https://cr.console.aliyun.com"
        echo "    2. 中科大: https://docker.mirrors.ustc.edu.cn"
        echo "    3. Docker Proxy: https://docker.1panel.live"
        echo "  ─────────────────────────────────────"
        printf "  是否继续? [y/N] "
        read -r proceed
        [ "$proceed" = "y" ] || [ "$proceed" = "Y" ] || exit 1
    fi
}

setup_secrets() {
    if [ ! -f .env.secrets ]; then
        if [ -f .env.secrets.example ]; then
            cp .env.secrets.example .env.secrets
            warn ".env.secrets 已从模板创建，请编辑填入真实密钥后重新运行"
            exit 1
        fi
    fi

    if [ ! -f secrets/db_password.txt ]; then
        warn "secrets/db_password.txt 不存在，使用默认值"
    fi
    if [ ! -f secrets/mysql_root_password.txt ]; then
        warn "secrets/mysql_root_password.txt 不存在，使用默认值"
    fi
}

check_health() {
    local url=${1:-http://localhost:8080/api/v1/auth/login}
    local retries=${2:-30}
    local delay=${3:-2}

    log "等待服务就绪: $url"
    for i in $(seq 1 "$retries"); do
        if curl -sf -o /dev/null "$url" 2>/dev/null; then
            log "服务健康检查通过"
            return 0
        fi
        sleep "$delay"
    done
    err "健康检查超时"
    return 1
}

cmd_dev() {
    log "启动开发环境..."
    check_deps
    docker compose -f compose.yaml up -d --build
    log "前端: http://localhost:${FRONTEND_PORT:-5173}"
    log "后端: http://localhost:${BACKEND_PORT:-8080}"
    log "源码挂载已启用，修改代码实时生效"
}

cmd_prod() {
    log "启动生产环境..."
    check_deps
    setup_secrets
    docker compose -f compose.prod.yaml up -d --build
    log "正在健康检查..."
    check_health
    log "部署完成 — http://localhost:${FRONTEND_PORT:-80}"
}

cmd_stop() {
    local file=${1:-compose.yaml}
    log "停止服务..."
    docker compose -f "$file" down --remove-orphans 2>/dev/null || true
    docker compose -f compose.prod.yaml down --remove-orphans 2>/dev/null || true
    docker compose -f compose.yaml down --remove-orphans 2>/dev/null || true
    log "所有服务已停止"
}

cmd_restart() {
    local env=${1:-dev}
    cmd_stop
    if [ "$env" = "prod" ]; then
        cmd_prod
    else
        cmd_dev
    fi
}

cmd_logs() {
    local svc=${1:-}
    if [ -n "$svc" ]; then
        docker compose logs -f "$svc"
    else
        docker compose logs -f
    fi
}

cmd_status() {
    log "容器状态:"
    docker compose -f compose.yaml ps 2>/dev/null || true
    docker compose -f compose.prod.yaml ps 2>/dev/null || true
}

cmd_backup() {
    local backup_file="backup_$(date +%Y%m%d_%H%M%S).sql"
    log "备份数据库到 $backup_file ..."
    docker exec -e MYSQL_PWD="${MYSQL_ROOT_PASSWORD:?MYSQL_ROOT_PASSWORD not set}" \
        wisdom-mysql mysqldump -u root \
        --single-transaction --routines --triggers property_ms > "$backup_file"
    log "备份完成: $backup_file ($(du -h "$backup_file" | cut -f1))"
}

cmd_clean() {
    warn "将删除所有容器、卷和构建缓存"
    read -rp "确认继续? [y/N] " confirm
    [ "$confirm" = "y" ] || [ "$confirm" = "Y" ] || exit 0

    docker compose -f compose.yaml       down -v --remove-orphans 2>/dev/null || true
    docker compose -f compose.prod.yaml  down -v --remove-orphans 2>/dev/null || true
    docker builder prune -af
    log "清理完成"
}

# ---- 入口 ----
case "${1:-}" in
    dev)     cmd_dev ;;
    prod)    cmd_prod ;;
    stop)    cmd_stop ;;
    restart) cmd_restart "${2:-dev}" ;;
    logs)    cmd_logs "${2:-}" ;;
    status)  cmd_status ;;
    backup)  cmd_backup ;;
    clean)   cmd_clean ;;
    *)
        echo "用法: $0 {dev|prod|stop|restart|logs|status|backup|clean}"
        echo ""
        echo "  dev       启动开发环境（热重载）"
        echo "  prod      启动生产环境（资源限制 + 密钥管理）"
        echo "  stop      停止所有服务"
        echo "  restart   重启服务"
        echo "  logs      查看容器日志"
        echo "  status    查看容器状态"
        echo "  backup    备份数据库"
        echo "  clean     清理所有容器和卷"
        exit 1
        ;;
esac
