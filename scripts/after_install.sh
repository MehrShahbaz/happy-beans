#!/bin/bash

echo ">>> [BeforeInstall] Fixing ownership..."
sudo chown -R ubuntu:ubuntu /home/ubuntu/app

echo ">>> [AfterInstall] Setting permissions for jar..."
chmod +x /home/ubuntu/app/build/libs/*.jar
