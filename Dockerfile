# Use a base image with Java 23 (required for Spring Boot 3.4.2)
FROM eclipse-temurin:23-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/mini1.jar /app/mini1.jar

# Create the data directory and copy JSON files
RUN mkdir -p /app/data
COPY src/main/java/com/example/data/*.json /app/data/

# Expose the application port
EXPOSE 8080

CMD ["java", "-jar", "/app/mini1.jar"]
