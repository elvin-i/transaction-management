FROM local/maven:3.9.3-jdk17 AS build
WORKDIR /app
COPY . /app
RUN mkdir -p /root/.m2 && cp /app/.mvn/settings.xml /root/.m2/settings.xml
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests

FROM openjdk:17-jdk-bullseye
WORKDIR /app
COPY --from=build /app/target/transaction-management-backend-web.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
