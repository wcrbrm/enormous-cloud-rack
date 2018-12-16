#!/bin/bash
sudo docker exec -it $(sudo docker ps | grep postgres | cut -d " " -f1) psql -U test -d rack
