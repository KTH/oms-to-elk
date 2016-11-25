FROM node:4-alpine

EXPOSE 3001

ENV NODE_ENV production
ENV OMS_ELK_TIMESTAMP_PATH /opt/data/timestamp.json
ENV LOGSTASH_CERT_PATH /opt/data/client.crt
ENV LOGSTASH_CA_PATH /opt/data/ca.crt

WORKDIR /opt/data
WORKDIR /opt/app
COPY [".", "."]

RUN apk add --no-cache --virtual .build-deps git \
    && npm install \
    && npm prune \
    && apk del .build-deps

CMD [ "npm", "start" ]