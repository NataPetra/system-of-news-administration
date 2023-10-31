# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-alpine
COPY --from=build /home/gradle/src/build/libs/news-comments-service-1.0.0.jar /app/news-comments-service-1.0.0.jar
ENTRYPOINT ["java", "-jar", "/app/news-comments-service-1.0.0.jar"]