## Create a stage for resolving and downloading dependencies.
#FROM eclipse-temurin:21-jdk-jammy AS deps
#
#WORKDIR /build
#
#COPY --chmod=0755 mvnw mvnw
#COPY .mvn/ .mvn/
#
#RUN --mount=type=bind,source=pom.xml,target=pom.xml \
#    --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests
#
#
#FROM deps AS package
#
#WORKDIR /build
#
#COPY ./src src/
#RUN --mount=type=bind,source=pom.xml,target=pom.xml \
#    --mount=type=cache,target=/root/.m2 \
#    ./mvnw package -DskipTests && \
#    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar
#
#################################################################################
#
#FROM package AS extract
#
#WORKDIR /build
#
#RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted
#
#################################################################################
#
#FROM eclipse-temurin:21-jre-jammy AS final
#
#ARG UID=10001
#RUN adduser \
#    --disabled-password \
#    --gecos "" \
#    --home "/nonexistent" \
#    --shell "/sbin/nologin" \
#    --no-create-home \
#    --uid "${UID}" \
#    appuser
#USER appuser
#
#COPY --from=extract build/target/extracted/dependencies/ ./
#COPY --from=extract build/target/extracted/spring-boot-loader/ ./
#COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
#COPY --from=extract build/target/extracted/application/ ./
#
#EXPOSE 8080
#
#ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]
# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

ENV spring.profiles.active=personal
# Copy the JAR file into the container
COPY target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
