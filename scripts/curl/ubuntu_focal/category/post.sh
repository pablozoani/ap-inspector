#!/bin/bash
DESCRIPTION=$1
curl http://localhost:9090/api/v1/categories --verbose --request POST --header "Content-Type: application/json" --data "{\"name\":\"$DESCRIPTION\"}"