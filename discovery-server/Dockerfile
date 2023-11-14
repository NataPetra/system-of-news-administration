# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /discovery-server
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-alpine

COPY --from=build /discovery-server/build/libs/discovery-server-1.0.0.jar /app/discovery-server-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/discovery-server-1.0.0.jar"]