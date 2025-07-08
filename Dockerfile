# Use lightweight Java 21 image
FROM eclipse-temurin:21-jdk-alpine

# Create working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/mailapp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
