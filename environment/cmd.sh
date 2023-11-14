#!/bin/bash

docker-compose -f docker-compose-light.yml up -d --force-recreate
docker-compose -f docker-compose-light.yml down

