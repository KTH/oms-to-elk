#!/bin/bash

set -e

if [ "$*" = "start" ]; then
    exec npm -s start
fi

exec $*
