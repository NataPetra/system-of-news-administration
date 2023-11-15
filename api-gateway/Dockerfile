# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /api-gateway
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-alpine

COPY --from=build /api-gateway/build/libs/api-gateway-1.0.0.jar /app/api-gateway-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/api-gateway-1.0.0.jar"]