#!/bin/bash

# Define log file for debugging
LOG_DIR="/home/ubuntu/app/logs"
LOG_FILE="$LOG_DIR/after_install.log"

# Ensure log directory exists
mkdir -p "$LOG_DIR"
chown -R ubuntu:ubuntu "$LOG_DIR"
touch "$LOG_FILE"
chmod 644 "$LOG_FILE"

echo "[$(date '+%Y-%m-%d %H:%M:%S')] Starting after_install.sh" >> "$LOG_FILE"

echo ">>> [AfterInstall] Fixing ownership..." >> "$LOG_FILE"
sudo chown -R ubuntu:ubuntu /home/ubuntu/app
echo ">>> Ownership of /home/ubuntu/app fixed." >> "$LOG_FILE"

sudo chown ubuntu:ubuntu /home/ubuntu/app/logs
echo ">>> Ownership of /home/ubuntu/app/logs fixed." >> "$LOG_FILE"

echo ">>> [AfterInstall] Setting permissions for jar..." >> "$LOG_FILE"
chmod +x /home/ubuntu/app/build/libs/*.jar
echo ">>> Jar files are now executable." >> "$LOG_FILE"

echo ">>> [AfterInstall] Setting execute permissions for all scripts..." >> "$LOG_FILE"
chmod +x /home/ubuntu/app/scripts/*.sh
echo ">>> Script files are now executable." >> "$LOG_FILE"

echo ">>> [Authbind] Configuring authbind for port 80..." >> "$LOG_FILE"
sudo touch /etc/authbind/byport/80
sudo chown ubuntu /etc/authbind/byport/80
sudo chmod 755 /etc/authbind/byport/80
echo ">>> Authbind setup complete for port 80." >> "$LOG_FILE"

echo "[$(date '+%Y-%m-%d %H:%M:%S')] Finished after_install.sh" >> "$LOG_FILE"
