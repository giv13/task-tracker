FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY common/pom.xml ./common/pom.xml
RUN cd common && mvn dependency:go-offline -B

COPY common/src ./common/src
RUN cd common && mvn install -DskipTests -B

COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

COPY backend/src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]