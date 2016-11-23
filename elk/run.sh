#!/bin/bash
#
# Helper script to create docker container with ELK stack.
#
dir=${0%/*}

docker run \
  -p 5601:5601 \
  -p 9200:9200 \
  -p 5044:5044 \
  -p5000:5000 \
  -v ${dir}/01-lumberjack-input.conf:/etc/logstash/conf.d/01-lumberjack-input.conf \
  -it \
  --name elk \
  sebp/elk:es241_l240_k461
