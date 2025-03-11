# Use a base image with Java 23 (required for Spring Boot 3.4.2)
FROM eclipse-temurin:23-jdk

# Set working directory
WORKDIR /app

COPY ./ ./

EXPOSE 8080

CMD ["java", "-jar", "./target/mini1.jar"]
