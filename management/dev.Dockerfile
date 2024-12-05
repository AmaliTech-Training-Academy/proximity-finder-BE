FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

ENV spring.profiles.active=docker
# Copy the JAR file into the container
COPY target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
