#!/bin/bash
set -e

echo ">>> [BeforeInstall] Stopping current Spring Boot application if running..."
pkill -f 'java -jar' || true

echo ">>> [BeforeInstall] Stopping Docker container if running..."
if [ $(docker ps -q -f name=happy-beans) ]; then
    docker stop happy-beans
    docker rm happy-beans
fi

echo ">>> [BeforeInstall] Fixing ownership..."
sudo chown -R ubuntu:ubuntu /home/ubuntu/app

echo ">>> [BeforeInstall] Cleaning old files..."
rm -rf /home/ubuntu/app/*
