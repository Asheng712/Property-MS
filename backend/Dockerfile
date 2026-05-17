# ============================================
# Stage 1 — 编译（含 Maven 依赖缓存层）
# ============================================
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /build

# 先只拷贝 pom.xml，利用 Docker 缓存机制避免每次重新下载依赖
COPY pom.xml ./
COPY wisdom-common/pom.xml ./wisdom-common/
COPY wisdom-pojo/pom.xml   ./wisdom-pojo/
COPY wisdom-server/pom.xml ./wisdom-server/
RUN mvn dependency:go-offline -pl . -B -q

# 拷贝源码并编译（跳过测试）
COPY . .
RUN mvn package -pl wisdom-server -am -DskipTests -B -q

# ============================================
# Stage 2 — 运行（精简 JRE 镜像）
# ============================================
FROM eclipse-temurin:17-jre-alpine

# 创建非 root 用户
RUN addgroup -S app && adduser -S app -G app

# 时区 & 字体（Knife4j/Swagger 图表可能需要）
RUN apk add --no-cache tzdata ttf-dejavu && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

USER app
COPY --from=build /build/wisdom-server/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75", \
    "-XX:InitialRAMPercentage=50", \
    "-XX:+UseG1GC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "/app/app.jar"]
