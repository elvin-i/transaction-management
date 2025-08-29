FROM local/maven:3.9.3-jdk17 AS build

WORKDIR /app
COPY . /app
RUN mkdir -p /root/.m2
RUN mvn clean package

FROM openjdk:17-jdk-bullseye
WORKDIR /app
COPY --from=build /app/target/transaction-management-backend-web.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
