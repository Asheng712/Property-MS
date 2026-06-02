# 监控配置贡献说明

姓名：王林峰
学号：2312190519
日期：2026-06-02

## 我完成的工作

### 1. 日志配置

- [x] 结构化日志格式（JSON）
- [x] 日志级别配置
- [x] 控制台 + 文件双输出
- [x] 按天滚动归档，保留30天

使用 Logback 的 `JsonEncoder` 实现结构化日志输出，无需额外依赖。日志同时输出到控制台和 `logs/app.log` 文件，方便开发调试和生产日志收集。

### 2. 健康检查

- [x] `/health` 端点实现
- [x] 数据库连接检测
- [x] 服务版本信息

健康检查端点返回服务状态（healthy）、时间戳、版本号和数据库连接状态。数据库检测使用 `Connection.isValid(3)` 方法，3秒超时，不会阻塞请求。

### 3. 指标收集

- [x] 请求计数
- [x] 响应时间
- [x] 错误率

通过 `MetricsInterceptor` 拦截器实现，使用 `AtomicLong` 保证线程安全。`/health` 和 `/metrics` 端点自动排除，不计入指标统计。

## PR 链接

- PR #X: https://github.com/Asheng712/Property-MS/pull/X

## 遇到的问题和解决

1. 问题：Spring Boot 默认的文本格式日志不支持结构化查询。
   解决：使用 Logback 内置的 `JsonEncoder`，无需引入额外依赖即可生成 JSON 格式日志。

2. 问题：拦截器可能阻塞监控端点自身的响应。
   解决：在 `WebMvcConfig` 中通过 `excludePathPatterns` 排除 `/health` 和 `/metrics`。

## 心得体会

监控是生产系统的重要组成部分。通过本次配置，我理解了结构化日志对日志收集和分析的重要性——JSON 格式的日志可以被 ELK、Splunk 等工具直接解析。健康检查端点则是容器编排（如 Kubernetes）进行服务健康探测的基础，而指标收集为后续接入 Prometheus 等监控系统打下了基础。
