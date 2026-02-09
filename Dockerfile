# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/FoodCampus-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run with production profile
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

