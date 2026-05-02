# Build stage
FROM gradle:8.5-jdk17 AS build
# Copy the project files
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Grant execution permissions to the wrapper (just in case)
RUN chmod +x gradlew

# Build using the wrapper for better compatibility
RUN ./gradlew bootJar -x test --no-daemon

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080

# Clean environment variables for Spring
ENTRYPOINT ["java", "-jar", "app.jar"]