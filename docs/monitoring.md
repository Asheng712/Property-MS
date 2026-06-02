# 系统监控配置说明

## 概述

本文档描述了智慧物业管理系统(WisdomPM)的监控配置，涵盖**后端**（Java/Spring Boot）和**前端**（Vue 3 + Vite）的结构化日志、健康检查和基础指标收集。

---

## 一、后端监控

### 1. 结构化日志

项目使用 Logback 的 `JsonEncoder` 实现结构化 JSON 日志输出。

#### 配置文件

`backend/wisdom-server/src/main/resources/logback-spring.xml`

#### 日志输出位置

| 输出目标 | 路径                         | 说明            |
| -------- | ---------------------------- | --------------- |
| 控制台   | stdout                       | JSON 格式实时输出 |
| 文件     | `logs/app.log`               | JSON 格式滚动文件 |
| 归档文件 | `logs/app.YYYY-MM-DD.log`    | 按天滚动，保留30天 |

#### JSON 日志格式示例

```json
{
  "timestamp": "2026-06-02T10:30:00.000+0800",
  "level": "INFO",
  "logger": "com.wisdom.controller.MonitorController",
  "message": "Health check requested",
  "thread": "http-nio-8080-exec-1"
}
```

### 2. 健康检查端点

#### 端点信息

- **URL**: `GET /health`
- **响应格式**: JSON
- **无需认证**

#### 响应字段

| 字段        | 类型   | 说明                |
| ----------- | ------ | ------------------- |
| status      | string | 服务状态: `healthy` |
| timestamp   | string | ISO 8601 时间戳     |
| version     | string | 应用版本号          |
| database    | string | 数据库连接状态      |

#### 响应示例

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

### 3. 基础指标收集

#### 指标端点

- **URL**: `GET /metrics`
- **无需认证**

#### 收集的指标

| 指标                   | 说明                |
| ---------------------- | ------------------- |
| totalRequests          | 总请求数            |
| totalErrors            | 总错误数(4xx/5xx)    |
| errorRate              | 错误率百分比        |
| averageResponseTimeMs  | 平均响应时间(毫秒)   |
| uptimeSeconds          | 服务运行时长(秒)     |

#### 响应示例

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

#### 实现方式

通过 `MetricsInterceptor` 拦截器自动记录每个 HTTP 请求的响应时间和状态码。`/health` 和 `/metrics` 端点自身不计入指标统计。

### 4. 后端代码结构

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

---

## 二、前端监控

### 1. 结构化日志

在 `frontend/src/utils/logger.ts` 中实现了 JSON 格式的结构化日志工具 `createLogger`。

#### 日志格式

```json
{
  "time": "2026-06-02T12:00:00.000Z",
  "level": "info",
  "message": "request recorded",
  "module": "monitor",
  "data": { "duration": 120, "success": true, "total": 42 }
}
```

| 字段      | 说明                                       |
| --------- | ------------------------------------------ |
| `time`    | ISO 8601 格式的时间戳                      |
| `level`   | 日志级别：debug / info / warn / error      |
| `message` | 日志消息文本                               |
| `module`  | 产生日志的模块名称                         |
| `data`    | 可选的附加数据，仅在传入时包含              |

#### 日志级别

- **debug**: 仅开发模式下输出，用于调试信息
- **info**: 常规操作记录（API 请求、状态变更等）
- **warn**: 警告信息（非致命问题）
- **error**: 错误信息（需要关注的问题）

#### 日志持久化

生产环境下日志自动写入 `sessionStorage`，最大保留 200 条记录，超出后自动轮转（清除最早的 50% 记录）。

### 2. 健康检查

前端通过 `GET /health` 端点检测后端服务状态，并在系统监控页面 (`/monitor`) 集中展示。

### 3. 指标收集

在 `frontend/src/utils/monitor.ts` 中实现了前端性能指标收集工具。

| 指标           | 说明                                 | 获取方法                           |
| -------------- | ------------------------------------ | ---------------------------------- |
| 请求总数       | 自页面加载以来的 API 请求总次数      | `monitor.getRequestCount()`        |
| 成功/失败计数  | 按结果区分的请求数量                 | `monitor.getMetrics().requests`    |
| 错误率         | 失败请求占总请求的百分比             | `monitor.getErrorRate()`           |
| 平均响应时间   | 所有请求的平均响应时间（毫秒）       | `monitor.getAverageResponseTime()` |

所有通过 `http.ts` 的 `request()` 函数发起的 API 请求会自动记录耗时和状态，在 `finally` 块中确保必然执行。

#### 系统健康评估

`getSystemHealth()` 函数基于收集的指标自动判断：
- **healthy**: 错误率 < 10% 且平均响应时间 < 3000ms
- **degraded**: 不满足上述任一条件

### 4. 前端监控页面

路径 `/monitor`，提供：实时指标卡片、后端健康详情面板、前端指标详情表格、系统综合评估面板。

### 5. 前端文件清单

| 文件                                    | 说明                   |
| --------------------------------------- | ---------------------- |
| `frontend/src/utils/logger.ts`          | 结构化 JSON 日志工具   |
| `frontend/src/utils/monitor.ts`         | 性能指标收集工具       |
| `frontend/src/services/http.ts`         | 集成请求自动监控       |
| `frontend/src/services/api.ts`          | 添加健康检查 API       |
| `frontend/src/views/monitor/MonitorView.vue` | 系统监控页面       |
| `frontend/src/router/index.ts`          | 添加 /monitor 路由     |
| `frontend/src/mock/data.ts`             | 添加系统监控导航菜单   |
