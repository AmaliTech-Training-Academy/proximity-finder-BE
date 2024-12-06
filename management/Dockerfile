FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy only the necessary files for downloading dependencies
COPY pom.xml .

# Download dependencies (cached if no changes in pom.xml)
RUN mvn dependency:go-offline

# Copy the entire source code
COPY src ./src

# Build the application
RUN mvn clean package -Dmaven.test.skip=true

# Step 2: Runtime stage
FROM openjdk:21-jdk-slim

# Create a non-root user and group
RUN groupadd -r management-group && useradd -r -g management-group management-user


# Set working directory inside the container
WORKDIR /app

#COPY --from=build /app/.env .
# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership of the /app directory to the non-root user
RUN chown -R management-user:management-group /app

# Switch to non-root user
USER management-user

# Expose the port the application runs on
EXPOSE 9092

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

