#!/bin/bash
echo ">>> [BeforeInstall] Stopping current application if running..."
pkill -f 'java -jar' || true

echo ">>> [BeforeInstall] Fixing ownership..."
sudo chown -R ubuntu:ubuntu /home/ubuntu/app

echo ">>> [BeforeInstall] Cleaning old files..."
rm -rf /home/ubuntu/app/*

