FROM eclipse-temurin:21-jdk AS build

LABEL authors="Adriana"

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

RUN ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar userservice.jar

RUN useradd app
USER app

EXPOSE 9090

CMD ["java", "-jar", "userservice.jar"]