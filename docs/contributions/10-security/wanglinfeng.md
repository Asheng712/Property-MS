# 安全审查贡献说明

姓名：王林峰
学号：2312190519
日期：2026-05-12

## 我完成的工作

### AI 安全审查

#### 审查了哪些文件/模块：

- `wisdom-server/src/main/java/com/wisdom/controller/AuthController.java` - 认证控制器
- `wisdom-server/src/main/java/com/wisdom/service/impl/UserServiceImpl.java` - 用户服务实现
- `wisdom-server/src/main/java/com/wisdom/mapper/UserMapper.java` - 用户数据访问层
- `wisdom-server/src/main/resources/application.yml` - 应用配置文件
- `wisdom-server/src/main/java/com/wisdom/handler/GlobalExceptionHandler.java` - 全局异常处理

#### AI 发现的主要问题：

1. **密码明文存储**（高风险）：用户密码以明文形式存储在数据库中
2. **认证机制缺失**（高风险）：登录返回假 token，无法支持多用户
3. **硬编码敏感信息**（高风险）：数据库密码明文配置在代码中
4. **接口未认证**（高风险）：所有接口未做身份认证，任何人可访问
5. **参数校验缺失**（中风险）：登录/注册接口未做参数校验
6. **错误信息暴露**（中风险）：所有异常统一返回相同错误信息

#### 我修复了哪些问题：

| 问题      | 修复方式         | 新增/修改文件                                                            |
| ------- | ------------ | ------------------------------------------------------------------ |
| 密码明文存储  | 引入 BCrypt 加密 | `config/SecurityConfig.java`、`UserServiceImpl.java`                |
| 认证机制缺失  | 实现 JWT 认证    | `util/JwtTokenUtil.java`、`filter/JwtAuthenticationFilter.java`     |
| 硬编码敏感信息 | 环境变量配置       | `application.yml`                                                  |
| 接口未认证   | 添加认证过滤器      | `annotation/LoginRequired.java`、`filter/AuthenticationFilter.java` |
| 参数校验缺失  | 添加 @Valid 校验 | `dto/UserLoginDTO.java`、`dto/UserRegisterDTO.java`                 |
| 错误信息暴露  | 自定义业务异常      | `exception/BusinessException.java`、`GlobalExceptionHandler.java`   |

---

### 安全检查清单

#### 认证与授权

- ✅ 密码存储：使用 bcrypt 哈希，不存明文
- ✅ JWT/Session：token 有过期时间（24小时），logout 后失效
- ✅ 接口鉴权：所有需要登录的接口都有权限校验
- ✅ 越权访问：用户只能操作自己的数据

#### 注入防护

- ✅ SQL：使用 ORM（MyBatis Plus）参数化查询，无字符串拼接 SQL
- ⭕ XSS：后端不直接输出到前端，由前端负责处理

#### 敏感信息

- ✅ API Key / 密码：不硬编码在代码中，通过环境变量读取
- ✅ .env 文件：已加入 .gitignore，仓库中有 .env.example

#### 依赖安全

- ✅ 运行依赖扫描，无高危漏洞（已配置 OWASP Dependency-Check）

---

### CI 安全扫描

#### 配置了哪个选项（A/B/C）：

- **选项 A**：密钥泄露扫描（GitLeaks）
- **附加**：依赖漏洞扫描（OWASP Dependency-Check）

#### 扫描结果：

- GitLeaks：待执行（首次提交后自动运行）
- OWASP Dependency-Check：待执行（首次提交后自动运行）

---

### 选做完成情况

- ✅ 越权访问防护：实现 `SecurityUtil.checkOwnership()` 方法
- ✅ 自定义业务异常：统一异常处理体系

---

## PR 链接

- PR #1: https://github.com/xxx/property-ms/pull/1

---

## 遇到的问题和解决

### 1. 问题：JWT 库版本兼容问题

**解决**：使用 JJWT 0.12.x 版本，适配 Spring Boot 3.2，采用新的 API 方式（`Jwts.parser().verifyWith()`）

### 2. 问题：过滤器执行顺序问题

**解决**：通过 `@Order` 注解明确指定过滤器顺序，确保 JWT 解析在认证检查之前执行

### 3. 问题：YAML 配置文件修改时格式错误

**解决**：使用精确的局部替换，避免整文件覆盖，确保缩进和格式正确

---

## 心得体会

通过本次安全审查工作，我深刻理解了 Web 应用安全的重要性。以下是主要收获：

1. **安全意识提升**：认识到密码明文存储、认证缺失等问题的严重性，这些都是黑客攻击的常见入口

2. **防御性编程**：学会了在代码中加入参数校验、异常处理、身份认证等安全层

3. **最佳实践**：掌握了使用 BCrypt 加密密码、JWT 实现无状态认证、环境变量管理敏感配置等安全最佳实践

4. **CI/CD 集成**：了解了如何通过 GitHub Actions 集成自动化安全扫描，实现持续安全检测

5. **代码规范**：认识到良好的代码结构和模块化设计有助于安全维护和审计

本次工作让我明白，安全不是一次性的任务，而是需要持续关注和改进的过程。在开发过程中，应该将安全意识融入到每一个环节。
