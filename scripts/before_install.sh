#!/bin/bash
echo ">>> [BeforeInstall] Stopping current application if running..."
sudo pkill -f 'java -jar' || true

echo ">>> [BeforeInstall] Fixing ownership..."
sudo chown -R ubuntu:ubuntu /home/ubuntu/app

echo ">>> Removing old files"
rm -rf /home/ubuntu/app/*
