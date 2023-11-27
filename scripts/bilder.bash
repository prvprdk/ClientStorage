#!/bin/bash

cd ./
mvn clean
mvn install -Pproduction
mvn package
echo 'DOCKER BUILD'
docker build -t farrokh4/client-storage .
echo 'DOCKER PUSH'
docker push farrokh4/client-storage
