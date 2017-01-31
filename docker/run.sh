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
resource_group=${AZURE_RESOURCE_GROUP}
oms_workspace=${AZURE_WORKSPACE}
clientId=${AZURE_CLIENTID}
tenantId=${AZURE_TENANTID}
clientKey=${AZURE_CLIENTKEY}
subscription=${AZURE_SUBSCRIPTION_ID}
eof

cat << eof > logstash.properties
# Logstash server connection details
keystore=${LOGSTASH_KEYSTORE}
server=${LOGSTASH_SERVER}
port=${LOGSTASH_PORT}
eof

cat << eof > oms-to-elk.properties
# oms-to-elk configurations
saved_query=${OMS_ELK_SAVED_QUERY}
eof

if [ ! -f "timestamp" ]; then
    case "${OMS_ELK_START_DATE:-'NOW'}" in
        ^[0-9][0-9TZ:-]*)
            echo "${OMS_ELK_START_DATE}" > timestamp
            ;;
    esac
fi

if [ "$*" = "start" ]; then
    exec java -cp /run/secrets:/opt/data:/opt/oms-to-elk/application.jar se.kth.integral.omstoelk.Run
fi

exec $*
