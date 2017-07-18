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
FROM openjdk:8-jre-alpine

RUN apk --no-cache add bash

ENV LOGSTASH_SERVER=localhost
ENV LOGSTASH_PORT=5000
ENV LOGSTASH_KEYSTORE=oms-to-elk.keystore
ENV OMS_ELK_SAVED_QUERY=oms-to-elk|Default

ADD oms-to-elk-*.jar /opt/oms-to-elk/application.jar
ADD run.sh /opt/oms-to-elk/run.sh
RUN chmod +x /opt/oms-to-elk/run.sh

WORKDIR /opt/data
ENTRYPOINT ["/opt/oms-to-elk/run.sh"]
CMD ["start"]
