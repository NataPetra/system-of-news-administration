# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /swagger-aggregator
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-alpine

COPY --from=build /swagger-aggregator/build/libs/swagger-aggregator-1.0.0.jar /app/swagger-aggregator-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/swagger-aggregator-1.0.0.jar"]