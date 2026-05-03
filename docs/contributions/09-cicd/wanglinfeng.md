# CI/CD 配置贡献说明

姓名：王林峰  学号：2312190519  角色：后端  日期：2026-04-29

## 完成的工作

### 工作流相关

- [x] 参与编写 / 审查 `.github/workflows/ci.yml`
- [x] 配置 Codecov 覆盖率上传（backend / frontend flag）
- [x] 添加 README 状态徽章

### 代码适配

- [x] 本地测试命令与 CI 一致，无需额外配置
- [x] 代码通过 Lint 检查（后端 Maven 编译校验 / 前端 vue-tsc）
- [x] 核心覆盖率达标（> 60%）

### 可选项

- [ ] 配置 Dependabot 自动更新依赖
- [ ] 集成 CodeRabbit AI 代码审查
- [ ] 使用 act 本地验证工作流

## PR 链接

- PR #X: https://github.com/Asheng712/Property-MS/pull/X

## CI 运行链接

- https://github.com/Asheng712/Property-MS/actions/workflows/ci.yml

## 遇到的问题和解决

1. 问题：作业示例使用 Python/pytest/ruff，但当前项目后端是 Spring Boot Maven 多模块。
   解决：将 backend job 适配为 JDK 17 + Maven，运行 `mvn -B -pl wisdom-server -am verify`，同时生成 JaCoCo XML。

2. 问题：前端项目没有单独的 ESLint 配置和 lint 脚本。
   解决：新增 `npm run lint`，使用 `vue-tsc -b --noEmit` 在 CI 中进行类型与模板静态检查。

3. 问题：前后端覆盖率报告路径不同。
   解决：后端上传 `backend/wisdom-server/target/site/jacoco/jacoco.xml`，前端上传 `frontend/coverage/lcov.info`，并分别设置 Codecov flag。

## 心得体会

本次 CI/CD 配置让我理解到工作流不能直接照搬模板，而要根据项目技术栈调整。后端 Maven、前端 Vite/Vitest 的测试与覆盖率产物不同，因此需要分别配置缓存、工作目录、测试命令和 Codecov 上传路径。通过把测试、静态检查和覆盖率上传放进同一个 CI 流水线，可以更早发现回归问题，也能让 README 徽章直观展示项目质量状态。
