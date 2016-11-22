FROM node:4-alpine

EXPOSE 3001

ENV NODE_ENV production

WORKDIR /usr/src/app
COPY [".", "."]

RUN apk add --no-cache --virtual .build-deps git \
    && npm install \
    && npm prune \
    && apk del .build-deps

CMD [ "npm", "start" ]