#!/bin/bash

EXECUTABLE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $EXECUTABLE_DIR

mvn clean package -DconfigPath=./ resources:resources liquibase:update -Plocal