# 软件测试贡献说明

姓名：钱富安  学号：2312190501  角色：前端  日期：2026-04-26

## 完成的测试工作

### 测试文件

- `frontend/src/components/__tests__/CoreComponents.test.ts`
- `frontend/src/services/http.test.ts`
- `frontend/src/utils/format.test.ts`

### 测试清单

- [x] 组件渲染 / 交互测试（8 个）
- [x] Mock API 请求测试（4 个以上，含失败场景）
- [x] 工具函数正常、边界情况测试（5 个）

### 覆盖率

- 执行命令：`npm run coverage`
- 覆盖率报告：`frontend/coverage/index.html`
- 覆盖率数据：`frontend/coverage/lcov.info`

### AI 辅助

- 使用工具：Codex
- Prompt 示例：`根据 hw.pdf 的前端成员任务，为 Vue + Vitest 项目补齐组件测试、Mock API 测试、覆盖率报告，并在 README 中展示覆盖率徽章。`
- AI 生成 + 人工修改的测试数量：13 个

## PR 链接

- 暂无

## 遇到的问题和解决

1. 问题：Vitest 在沙箱中启动时读取用户目录受限。解决：在允许权限下运行测试命令，并保证项目脚本仍为标准 `npm test` / `npm run coverage`。
2. 问题：组件依赖 Vue SFC 和浏览器环境。解决：引入 `@vue/test-utils` 与 `jsdom`，并将 Vitest 环境设置为 `jsdom`。

## 心得体会

本次测试补充更关注用户可见的渲染结果、表单交互和接口异常处理。通过组件测试和 Mock API 测试，可以在不依赖后端服务的情况下验证前端核心行为，覆盖率报告也便于后续持续集成追踪质量变化。
