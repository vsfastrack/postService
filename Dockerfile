# Use a base image with Java 8 and Gradle installed
FROM gradle:6.9.1-jdk8 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle files
COPY build.gradle .
COPY settings.gradle .
COPY gradle ./gradle

# Copy the source code to the container
COPY src ./src

# Build the application
RUN gradle build -x test --no-daemon

# Use a base image with Java 8 only for runtime
FROM adoptopenjdk:8-jre-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/post-service-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port on which your Spring Boot application listens
EXPOSE 8081

# Set any necessary environment variables
# ENV VARIABLE_NAME value

# Set the entry point command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
