# WisdomPM Backend

[![Backend Coverage](https://codecov.io/gh/Asheng712/Property-MS/branch/main/graph/badge.svg?flag=backend)](https://codecov.io/gh/Asheng712/Property-MS)

智慧物业管理系统后端，基于 Spring Boot 3、Maven 多模块与 MyBatis-Plus 构建。

## 覆盖率报告

```bash
mvn -pl wisdom-server -am verify
```

JaCoCo 报告生成位置：

```text
wisdom-server/target/site/jacoco/index.html
wisdom-server/target/site/jacoco/jacoco.xml
```

GitHub Actions 会在测试通过后将 `jacoco.xml` 上传到 Codecov，并使用 `backend` flag 区分后端覆盖率。
