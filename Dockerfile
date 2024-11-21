# Use a base image with Java 21 (or the version you are using)
FROM openjdk:21-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the jar file to the Docker image
COPY target/UserAssignmnet-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app is running on (default is 8080)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
