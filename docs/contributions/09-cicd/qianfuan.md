# CI/CD 配置贡献说明

姓名：钱富安 学号：2312190501  角色：前端  日期：2026-05-05

## 完成的工作

### 工作流相关

- [x] 参与编写 / 审查 `.github/workflows/ci.yml`
- [x] 配置 Codecov 覆盖率上传（backend / frontend flag）
- [x] 添加 README 状态徽章

### 代码适配

- [x] 本地测试命令与 CI 一致，`npm test` 直接生成覆盖率报告
- [x] 代码通过 Lint 检查（ESLint 零警告 + vue-tsc 类型检查）
- [x] 前端覆盖率报告生成 `frontend/coverage/lcov.info`

## PR 链接

- PR #X: https://github.com/Asheng712/Property-MS/pull/X

## CI 运行链接

- https://github.com/Asheng712/Property-MS/actions/workflows/ci.yml

## 遇到的问题和解决

1. 问题：作业示例使用 Python/pytest/ruff，但当前项目后端是 Spring Boot Maven 多模块。
   解决：将 backend job 适配为 JDK 17 + Maven，运行 `mvn -B -pl wisdom-server -am verify`，同时生成 JaCoCo XML。

2. 问题：前端项目没有单独的 ESLint 配置和 lint 脚本。
   解决：新增 ESLint flat config，并将 `npm run lint` 配置为 `eslint src --max-warnings 0 && vue-tsc -b --noEmit`，保证 CI 中静态检查零警告。

3. 问题：`npm test` 原本只运行 Vitest，不会生成 Codecov 需要的 lcov 报告。
   解决：将 `npm test` 改为 `vitest run --coverage`，CI frontend job 直接执行 `npm test` 并上传 `frontend/coverage/lcov.info`。

## 心得体会

本次 CI/CD 配置让我理解到工作流不能直接照搬模板，而要根据项目技术栈调整。当前前端是 Vue 3 + Vite + Vitest，因此需要把非交互式测试、覆盖率输出、ESLint 零警告和类型检查串到同一套 npm scripts 中。通过让本地命令与 CI 命令一致，可以更早发现回归问题，也能让 README 徽章直观展示项目质量状态。
