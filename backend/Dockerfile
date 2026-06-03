# ============================================
# Stage 1 — 共享依赖缓存层（deps）
# ============================================
FROM maven:3.9-eclipse-temurin-17-alpine AS deps
WORKDIR /app

# 阿里云 Maven 镜像加速
COPY settings.xml /root/.m2/settings.xml

# 先只拷贝 pom.xml，利用 Docker 缓存机制避免每次重新下载依赖
COPY pom.xml ./
COPY wisdom-common/pom.xml ./wisdom-common/
COPY wisdom-pojo/pom.xml   ./wisdom-pojo/
COPY wisdom-server/pom.xml ./wisdom-server/
RUN mvn dependency:go-offline -pl . -B -q

# ============================================
# Stage dev — 开发模式（spring-boot:run + DevTools 热重载）
# ============================================
FROM deps AS dev
COPY . .
EXPOSE 8080
CMD ["sh", "-c", "mvn install -N -DskipTests -q && mvn install -pl wisdom-common,wisdom-pojo -DskipTests -q && mvn spring-boot:run -pl wisdom-server -DskipTests -Dspring-boot.run.jvmArguments=-XX:+UseG1GC"]

# ============================================
# Stage build — 生产编译（打包可执行 JAR）
# ============================================
FROM deps AS build
COPY . .
RUN mvn package -pl wisdom-server -am -DskipTests -B -q

# ============================================
# Stage runtime — 生产运行（精简 JRE 镜像）
# ============================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# 创建非 root 用户
RUN addgroup -S app && adduser -S app -G app

# 时区 & 字体（Knife4j/Swagger 图表需要）+ wget（HEALTHCHECK 依赖）
RUN apk add --no-cache tzdata ttf-dejavu wget && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

USER app
COPY --from=build /app/wisdom-server/target/*.jar /app/app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=3 CMD wget -qO- http://127.0.0.1:8080/api/v1/auth/login || exit 1

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75", \
    "-XX:InitialRAMPercentage=50", \
    "-XX:+UseG1GC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "/app/app.jar"]
