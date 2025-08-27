#!/bin/bash
set -e

# Define the log directory
LOG_DIR="/home/ubuntu/app/logs"

# Log file path
LOG_FILE="$LOG_DIR/app_start.log"

# Define a simple logging function with a timestamp
log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_FILE"
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

# Clear the log file at the start of the script
echo "" > "$LOG_FILE"

log "Starting start.sh script"

# Ensure the log directory exists
if [ ! -d "$LOG_DIR" ]; then
  log "Creating logs directory at $LOG_DIR"
  mkdir -p "$LOG_DIR" || { log "ERROR: Failed to create log directory $LOG_DIR"; exit 1; }
else
  log "Logs directory already exists at $LOG_DIR"
fi

# Ensure correct permissions for the log directory
log "Setting ownership of log directory to ubuntu:ubuntu"
if ! sudo chown -R ubuntu:ubuntu "$LOG_DIR"; then
  log "WARNING: Failed to set ownership of $LOG_DIR (sudo may be required)"
fi

# Ensure the log file has correct permissions
log "Setting permissions on $LOG_FILE"
chmod 644 "$LOG_FILE" || { log "ERROR: Failed to set permissions on $LOG_FILE"; exit 1; }

# Change to the application directory
log "Changing directory to /home/ubuntu/app"
cd /home/ubuntu/app || { log "ERROR: Failed to change directory to /home/ubuntu/app"; exit 1; }

# Run the application using the private IP address
log "Starting the Spring Boot application with private IP address"
nohup authbind --deep java -jar /home/ubuntu/app/build/libs/happy-beans-0.0.1-SNAPSHOT.jar \
--spring.profiles.active=prod \
> "$LOG_DIR/app.log" 2>&1 &

# Get the PID of the new process
PID=$!

# Wait for the application to be healthy on port 80
log "Waiting for application to become healthy on port 80..."
HEALTH_CHECK_URL="http://localhost:80/api/health"
MAX_RETRIES=20
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
  if curl --fail --silent "$HEALTH_CHECK_URL" > /dev/null; then
    log "Application is healthy."
    exit 0
  fi
  log "Health check failed, retrying in 5 seconds..."
  sleep 5
  RETRY_COUNT=$((RETRY_COUNT + 1))
done

log "ERROR: Application failed to become healthy within the timeout period."
exit 1
