#!/bin/bash
#
# Helper script to create docker container with ELK stack.
# Has to be run from current directory, ./run.sh
#

image=sebp/elk:es241_l240_k461

if docker ps -a -f name=elk | grep -q $image; then
  docker start -a elk
else
  docker run \
    -p 5601:5601 \
    -p 9200:9200 \
    -p 5044:5044 \
    -p5000:5000 \
    -v `pwd`/conf.d:/etc/logstash/conf.d \
    -it \
    --name elk \
    $image
fi
