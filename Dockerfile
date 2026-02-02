FROM maven:3.9.6-openjdk-21-slim AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/dbrand-app-1.0.0.jar app.jar

# Environment variables for Nhost database
ENV DATABASE_URL=jdbc:postgresql://esntnswdctfpdxnscsuq.db.eu-central-1.nhost.run:5432/esntnswdctfpdxnscsuq
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=WM5uusBKzYUg14Dm
ENV JWT_SECRET=mySecretKeyForDbrandApplication1234567890

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]