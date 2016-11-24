#!/bin/bash
#
# Helper script to create docker container with ELK stack.
#

docker run \
  -p 5601:5601 \
  -p 9200:9200 \
  -p 5044:5044 \
  -p5000:5000 \
  -v .../conf.d:/etc/logstash/conf.d \
  -it \
  --name elk \
  sebp/elk:es241_l240_k461
