#!/bin/bash
DESCRIPTION=$1
curl http://localhost:8080/api/v1/categories --verbose --request POST --header "Content-Type: application/json" --data "{\"description\":\"$DESCRIPTION\"}"