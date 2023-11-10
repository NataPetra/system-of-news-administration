# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /user-service
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-alpine

COPY --from=build /user-service/build/libs/user-service-1.0.0.jar /app/user-service-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/user-service-1.0.0.jar"]
