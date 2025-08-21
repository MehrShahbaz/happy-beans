#!/bin/bash
set -e

# Go to app directory
cd /home/ubuntu/app

# Ensure Gradle Wrapper is executable
chmod +x ./gradlew

# Build the JAR
echo "Building Spring Boot JAR..."
./gradlew clean bootJar

# Build Docker image
echo "Building Docker image..."
sudo docker build -t happy-beans-image:1.0 .

echo "Docker image built successfully."
