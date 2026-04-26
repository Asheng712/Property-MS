# 软件测试贡献说明

姓名：王林峰  学号：2312190519  角色：后端  日期：2026-04-26

## 完成的测试工作

### 测试文件

- `wisdom-server/src/test/java/com/wisdom/service/impl/NoticeServiceImplTest.java`
- `wisdom-server/src/test/java/com/wisdom/service/impl/ComplaintServiceImplTest.java`
- `wisdom-server/src/test/java/com/wisdom/service/impl/UserServiceImplTest.java`
- `wisdom-server/src/test/java/com/wisdom/controller/AuthControllerApiTest.java`
- `wisdom-server/src/test/java/com/wisdom/controller/NoticeControllerApiTest.java`
- `wisdom-server/src/test/java/com/wisdom/controller/ComplaintControllerApiTest.java`

### 测试清单

- [x] 正常情况测试：公告分页查询、公告新增、公告更新、投诉分页查询、投诉处理、用户登录注册
- [x] 边界 / 异常情况测试：分页参数缺省、投诉不存在、用户不存在、密码错误、公告保存异常
- [x] Mock 使用：通过 Mockito Mock `NoticeMapper`、`ComplaintMapper`、`UserMapper` 以及 Controller 层依赖的 Service
- [x] 数据转换校验：验证 DTO -> Entity、Entity -> VO 的关键字段映射
- [x] 覆盖率流水线：配置 GitHub Actions 生成 JaCoCo XML 并上传 Codecov

### 覆盖率

- 后端核心模块覆盖率：97.22%
- 覆盖率命令：`mvn -pl wisdom-server -am verify`
- 覆盖率报告：`wisdom-server/target/site/jacoco/index.html`
- 覆盖率上传文件：`wisdom-server/target/site/jacoco/jacoco.xml`

## AI 辅助（如有）

- 使用工具：Codex
- Prompt 示例：请为 `NoticeServiceImpl` 和 `ComplaintServiceImpl` 补充单元测试，并将后端核心覆盖率提升到 60% 以上。
- AI 生成 + 人工修改的测试数量：8 个
- 人工确认内容：检查测试断言、Mapper Mock 行为、JaCoCo 覆盖率命令和 GitHub Actions 上传路径

## PR 链接

- PR #X：https://github.com/Asheng712/Property-MS/pull/X

## 遇到的问题和解决

1. 问题：Codecov 初始后端覆盖率只有 43%。
   解决：发现 workflow 使用 `test jacoco:report` 时会生成 23 个类的宽口径报告，改为 `mvn -pl wisdom-server -am verify`，使用 POM 中配置的核心类 include 口径。

2. 问题：`NoticeServiceImpl` 和 `ComplaintServiceImpl` 在 JaCoCo check 中覆盖率为 0%。
   解决：补充 Service 层单元测试，覆盖分页查询、新增、更新、处理成功、处理异常等路径。

3. 问题：本地 `.mvnrepo` 存在 0 字节 POM，导致 Maven 无法读取依赖。
   解决：清理损坏缓存文件后重新运行 Maven 校验。

## 心得体会

本次测试工作让我更清楚地理解了单元测试和覆盖率统计口径之间的关系。覆盖率偏低不一定是测试完全无效，也可能是 JaCoCo 报告包含了过多暂未测试的类。通过为核心 Service 补充正常路径、异常路径和 Mock 依赖测试，既提升了覆盖率，也让公告和投诉模块的业务行为更容易被回归验证。
