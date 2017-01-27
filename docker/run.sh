#!/bin/bash
#
# Copyright (c) 2017 Kungliga Tekniska högskolan
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#

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
