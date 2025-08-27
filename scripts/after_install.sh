#!/bin/bash

echo ">>> [BeforeInstall] Fixing ownership..."
sudo chown -R ubuntu:ubuntu /home/ubuntu/app
echo ">>> Ownership of /home/ubuntu/app fixed."
sudo chown ubuntu:ubuntu /home/ubuntu/app/logs
echo ">>> Ownership of /home/ubuntu/app/logs fixed."

echo ">>> [AfterInstall] Setting permissions for jar..."
chmod +x /home/ubuntu/app/build/libs/*.jar
echo ">>> Jar files are now executable."

echo ">>> [AfterInstall] Setting execute permissions for all scripts..."
chmod +x /home/ubuntu/app/scripts/*.sh
echo ">>> Script files are now executable."

echo ">>> [Authbind] Configuring authbind for port 80..."
sudo touch /etc/authbind/byport/80
sudo chown ubuntu /etc/authbind/byport/80
sudo chmod 755 /etc/authbind/byport/80
echo ">>> Authbind setup complete for port 80."
