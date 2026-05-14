# ── Stage 1: Build ────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and build files first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (cached unless build files change)
RUN ./gradlew dependencies --no-daemon 2>/dev/null || true

# Copy source code
COPY src src

# Build the JAR (skip tests — they run in CI)
RUN ./gradlew bootJar --no-daemon -x test

# ── Stage 2: Run ──────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S daybook && adduser -S daybook -G daybook

# Copy only the JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Switch to non-root user
USER daybook

# Expose the port Spring Boot listens on
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]