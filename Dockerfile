# Build stage
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Build the application skipping tests to speed up the process
RUN gradle build -x test --no-daemon

# Run stage
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]