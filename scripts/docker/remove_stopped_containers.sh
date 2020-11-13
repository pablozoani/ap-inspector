#!/bin/bash
docker container rm $(docker container list --all --quiet)
