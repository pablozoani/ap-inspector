#!/bin/bash
DESCRIPTION=$1
ID=$2
curl --verbose --request PUT --header "Content-Type: application/json" --data "{\"description\":\"$DESCRIPTION\"}" http://localhost:8080/api/v1/categories/"$ID"