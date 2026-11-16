# syntax=docker/dockerfile:1

# Stage 1: Build application
FROM maven:3.10-eclipse-temurin-25-alpine AS builder

WORKDIR /build

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Run as a non-privileged user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Copy artifact from builder stage
COPY --from=builder /build/target/omnigrocer-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
