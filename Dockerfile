# ---------- STAGE 1: Build the JAR ----------
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy source code into container
COPY . .

# Build the app (skipping tests for speed)
RUN mvn clean package -DskipTests


# ---------- STAGE 2: Run the app ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built JAR from previous stage
COPY --from=build /app/target/mailapp-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot's default port
EXPOSE 8081

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
