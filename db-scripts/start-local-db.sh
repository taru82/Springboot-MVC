#!/bin/bash -e

EXECUTABLE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "==============================================="
echo "*** INITIALIZING DOCKER ***"
docker-compose -f $EXECUTABLE_DIR/docker/docker-compose.yml up -d
echo "==============================================="

cd $EXECUTABLE_DIR


# Define a function to check if all services are up
check_docker_services() {
    docker-compose -f $EXECUTABLE_DIR/docker/docker-compose.yml ps -q | xargs docker inspect -f '{{ .State.Status }}' | grep -q "running"
}

# Check every 2 seconds if all services are up
while ! check_docker_services; do
    echo "Services not yet ready, waiting..."
    sleep 2
done

echo "*** CREATING SCHEMA AND ROLES ***"
./initialize-schema.sh
echo "==============================================="

#run liquibase
echo "*** RUNNING LIQUIBASE ***"
mvn clean package -DconfigPath=./ resources:resources liquibase:update -Plocal
echo "==============================================="
