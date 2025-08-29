# 构建阶段
FROM local/maven:3.9.3-jdk17 AS build
WORKDIR /app
COPY . /app
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jdk-bullseye
WORKDIR /app
COPY --from=build /app/target/transaction-management-backend-web.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
