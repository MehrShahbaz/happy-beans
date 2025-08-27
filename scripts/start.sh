#!/bin/bash
set -e

# Define the log directory
LOG_DIR="/home/ubuntu/app/logs"

# Log file path
LOG_FILE="$LOG_DIR/app_start.log"

# Define a simple logging function with a timestamp
log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_FILE"
}

# Ensure the log directory exists
if [ ! -d "$LOG_DIR" ]; then
  log "Creating logs directory at $LOG_DIR"
  mkdir -p "$LOG_DIR" || { log "ERROR: Failed to create log directory $LOG_DIR"; exit 1; }
else
  log "Logs directory already exists at $LOG_DIR"
fi

# Ensure correct permissions for the log directory
log "Setting ownership of log directory to ubuntu:ubuntu"
if ! chown -R ubuntu:ubuntu "$LOG_DIR"; then
  log "WARNING: Failed to set ownership of $LOG_DIR (sudo may be required)"
fi

# Clear or create the log file
log "Initializing log file at $LOG_FILE"
if [ -f "$LOG_FILE" ]; then
  : > "$LOG_FILE" || { log "ERROR: Failed to clear log file $LOG_FILE"; exit 1; }
else
  touch "$LOG_FILE" || { log "ERROR: Failed to create log file $LOG_FILE"; exit 1; }
fi

# Ensure the log file has correct permissions
chmod 644 "$LOG_FILE" || { log "ERROR: Failed to set permissions on $LOG_FILE"; exit 1; }

log "Starting start.sh script"

# Change to the application directory
log "Changing directory to /home/ubuntu/app"
cd /home/ubuntu/app || { log "ERROR: Failed to change directory to /home/ubuntu/app"; exit 1; }

# Run the application using the private IP address
log "Starting the Spring Boot application with private IP address"
nohup authbind --deep java -jar /home/ubuntu/app/build/libs/happy-beans-0.0.1-SNAPSHOT.jar \
--spring.profiles.active=prod \
#--spring.datasource.url=jdbc:postgresql://10.0.100.46:5432/happy_beans \
> "$LOG_DIR/app.log" 2>&1 &

# Get the PID of the new process
PID=$!

# Add a message to both the main log and the application log
log "Application started with PID: $PID. Logs are being redirected to $LOG_DIR/app.log"
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Script started new process with PID $PID" >> "$LOG_DIR/app.log"

log "Script finished."