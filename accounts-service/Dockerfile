FROM maven:3-amazoncorretto-24 AS build

WORKDIR /app

COPY pom.xml .
COPY accounts-service/ accounts-service/

WORKDIR /app/accounts-service

RUN mvn clean package -DskipTests

FROM eclipse-temurin:24-jdk

WORKDIR /app/accounts-service

COPY --from=build /app/accounts-service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
