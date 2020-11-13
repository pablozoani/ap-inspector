#!/bin/bash
ID=$1
curl http://localhost:8080/api/v1/categories/$ID --request GET --verbose