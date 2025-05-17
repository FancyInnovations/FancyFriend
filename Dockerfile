# Use the official Gradle image to build the application
FROM gradle:8.12-jdk23 AS builder

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the project (replace 'build' with your actual build task if different)
RUN ./gradlew shadowJar

# Use a lightweight JDK image for the runtime
FROM eclipse-temurin:23-jdk-alpine

# Set the working directory in the new image
WORKDIR /app

# Copy the built jar file from the builder stage
COPY --from=builder /app/build/libs/FancyFriend.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]