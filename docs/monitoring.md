# 监控配置说明

## 概述

本文档描述了智慧物业管理系统(WisdomPM)的监控配置，包括结构化日志、健康检查和基础指标收集。

## 1. 结构化日志

项目使用 Logback 的 `JsonEncoder` 实现结构化 JSON 日志输出。

### 配置文件

`backend/wisdom-server/src/main/resources/logback-spring.xml`

### 日志输出位置

| 输出目标   | 路径                | 说明           |
|--------|-------------------|--------------|
| 控制台    | stdout            | JSON 格式实时输出  |
| 文件     | `logs/app.log`    | JSON 格式滚动文件  |
| 归档文件   | `logs/app.YYYY-MM-DD.log` | 按天滚动，保留30天 |

### JSON 日志格式示例

```json
{
  "timestamp": "2026-06-02T10:30:00.000+0800",
  "level": "INFO",
  "logger": "com.wisdom.controller.MonitorController",
  "message": "Health check requested",
  "thread": "http-nio-8080-exec-1"
}
```

## 2. 健康检查端点

### 端点信息

- **URL**: `GET /health`
- **响应格式**: JSON
- **无需认证**

### 响应字段

| 字段        | 类型     | 说明                |
|-----------|--------|-------------------|
| status    | string | 服务状态: `healthy`    |
| timestamp | string | ISO 8601 时间戳      |
| version   | string | 应用版本号             |
| database  | string | 数据库连接状态           |

### 响应示例

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "status": "healthy",
    "timestamp": "2026-06-02T10:30:00.000Z",
    "version": "1.0.0",
    "database": "connected"
  }
}
```

## 3. 基础指标收集

### 指标端点

- **URL**: `GET /metrics`
- **无需认证**

### 收集的指标

| 指标               | 说明      |
|------------------|---------|
| totalRequests    | 总请求数    |
| totalErrors      | 总错误数(4xx/5xx) |
| errorRate        | 错误率百分比  |
| averageResponseTimeMs | 平均响应时间(毫秒) |
| uptimeSeconds    | 服务运行时长(秒) |

### 响应示例

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "totalRequests": 1523,
    "totalErrors": 12,
    "errorRate": "0.79%",
    "averageResponseTimeMs": "45.32",
    "uptimeSeconds": 86400
  }
}
```

### 实现方式

通过 `MetricsInterceptor` 拦截器自动记录每个 HTTP 请求的响应时间和状态码。`/health` 和 `/metrics` 端点自身不计入指标统计。

## 4. 代码结构

```
backend/wisdom-server/src/main/java/com/wisdom/
├── config/
│   └── WebMvcConfig.java          # 注册指标拦截器
├── controller/
│   └── MonitorController.java     # /health 和 /metrics 端点
├── metrics/
│   ├── MetricsInterceptor.java    # 请求指标拦截器
│   └── MetricsService.java        # 指标收集服务
└── resources/
    └── logback-spring.xml         # 结构化日志配置
```
