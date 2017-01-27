#!/bin/bash

set -e

cat << eof > azure.properties
# Azure OMS connection details
azure.clientId=${OMS_ELK_CLIENTID}
azure.tenantId=${OMS_ELK_CLIENTID}
azure.clientKey=${OMS_ELK_CLIENTKEY}
azure.subscription=${AZURE_SUBSCRIPTION_ID}
azure.resource_group=${AZURE_RESOURCE_GROUP}
azure.oms_workspace=${OMS_WORKSPACE}
# Logstash server connection details
logstash.keystore=${LOGSTASH_KEYSTORE}
logstash.server=${LOGSTASH_SERVER}
logstash.port=${LOGSTASH_PORT}
eof

if [ ! -f /opt/data/timestamp.json ]; then
    case "${OMS_ELK_START_DATE:-'NOW'}" in
        ^[0-9][0-9TZ:-]*)
            echo "startdate=${OMS_ELK_START_DATE}" > /opt/data/timestamp.properties
            ;;
        *)
            echo "startdate=" > /opt/data/timestamp.properties
            ;;
    esac
fi

while [ ! -f /opt/data/timestamp.properties ]; do
    echo "/opt/data/timestamp.properties not found, waiting 30s..."
    sleep 30
done

if [ "$*" = "start" ]; then
    exec java -cp /run/secrets:/opt/data -jar application.jar
fi

exec $*
