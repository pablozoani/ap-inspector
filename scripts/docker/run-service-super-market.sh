#!/bin/bash
docker container run\
  --name service-super-market\
  --net backend\
  --link super-market-database:mongo\
  --publish 8080:8080\
  pzoani/service-super-market:0.0.1