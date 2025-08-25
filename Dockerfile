# Use Eclipse Temurin Java 21 JRE base image
FROM eclipse-temurin:21-jre-jammy

# Copy your Spring Boot jar (adjust path and name accordingly)
COPY build/libs/happy-beans-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "/app.jar"]
