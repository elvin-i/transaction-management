# 使用 Maven 构建阶段
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 使用 JDK 运行阶段
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 只拷贝 web jar
COPY target/transaction-management-backend-web.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

