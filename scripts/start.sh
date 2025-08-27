#!/bin/bash
set -e

# Log file path
LOG_FILE="/home/ubuntu/app/logs"

# Define a simple logging function with a timestamp
log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_FILE"
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

# Clear the log file at the start of the script
echo "" > "$LOG_FILE"

log "Starting start.sh script"

# Change to the application directory
log "Changing directory to /home/ubuntu/app"
cd /home/ubuntu/app

# Ensure the logs directory exists and has correct permissions
LOG_DIR="/home/ubuntu/app/logs"
log "Ensuring logs directory exists at $LOG_DIR"
mkdir -p "$LOG_DIR"
log "Fixing ownership of log directory"
sudo chown -R ubuntu:ubuntu "$LOG_DIR"

# Run the application using the private IP address
log "Starting the Spring Boot application with private IP address"
nohup authbind --deep java -jar /home/ubuntu/app/build/libs/happy-beans-0.0.1-SNAPSHOT.jar \
--spring.profiles.active=prod \
--spring.datasource.url=jdbc:postgresql://10.0.100.46:5432/happy_beans \
> "$LOG_DIR"/app.log 2>&1 &

# Get the PID of the new process
PID=$!

# Add a message to both the main log and the application log
log "Application started with PID: $PID. Logs are being redirected to $LOG_DIR/app.log"
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Script started new process with PID $PID" >> "$LOG_DIR/app.log"

log "Script finished."