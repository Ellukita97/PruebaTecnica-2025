FROM maven:3-amazoncorretto-24 AS build
WORKDIR /app

COPY pom.xml .
COPY transactions-service/ transactions-service/

WORKDIR /app/transactions-service

RUN mvn clean package -DskipTests

FROM eclipse-temurin:24-jdk

WORKDIR /app/transactions-service

COPY --from=build /app/transactions-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

