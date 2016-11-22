#!/bin/bash

LOG_DIR=logs
PROJECT=monitor

export NODE_PATH=`pwd`

# Check parameter for type of env
if [ "$1" = "prod" ] ; then
    export NODE_ENV=production
    exec node app.js
elif [ "$1" = "ref" ] ; then
    export NODE_ENV=referens
    exec node app.js
else
    export NODE_ENV=development
    npm run installAndStart
fi
