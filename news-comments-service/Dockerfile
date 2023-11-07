# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /application-logging-starter
COPY application-logging-starter/build.gradle .
COPY application-logging-starter/settings.gradle .
COPY application-logging-starter/src ./src

WORKDIR /news-comments-service
COPY news-comments-service/build.gradle .
COPY news-comments-service/settings.gradle .
COPY news-comments-service/src ./src

RUN gradle -b /application-logging-starter/build.gradle build --no-daemon \
&&  gradle -b /news-comments-service/build.gradle build -x test --no-daemon

# Run stage
FROM openjdk:17-alpine
COPY --from=build /news-comments-service/build/libs/news-comments-service-1.0.0.jar /app/news-comments-service-1.0.0.jar
ENTRYPOINT ["java", "-jar", "/app/news-comments-service-1.0.0.jar"]