FROM maven:3-amazoncorretto-24 AS build

WORKDIR /app

COPY pom.xml .
COPY customers-service/ customers-service/

WORKDIR /app/customers-service

RUN mvn clean package -DskipTests

FROM eclipse-temurin:24-jdk

WORKDIR /app/customers-service

COPY --from=build /app/customers-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
