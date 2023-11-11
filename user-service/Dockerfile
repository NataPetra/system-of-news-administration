# Build stage
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /exception-handling-starter
COPY exception-handling-starter/build.gradle .
COPY exception-handling-starter/settings.gradle .
COPY exception-handling-starter/src ./src

WORKDIR /user-service
COPY user-service/build.gradle .
COPY user-service/settings.gradle .
COPY user-service/src ./src

RUN gradle -b /exception-handling-starter/build.gradle build --no-daemon \
&&  gradle -b /user-service/build.gradle build -x test --no-daemon

# Run stage
FROM openjdk:17-alpine

COPY --from=build /user-service/build/libs/user-service-1.0.0.jar /app/user-service-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/user-service-1.0.0.jar"]
