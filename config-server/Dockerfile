# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /config-server
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-alpine

COPY --from=build /config-server/build/libs/config-server-1.0.0.jar /app/config-server-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/config-server-1.0.0.jar"]
