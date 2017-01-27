FROM openjdk:8-jre-alpine

RUN apk --no-cache add bash

ENV LOGSTASH_KEYSTORE=oms-to-elk.keystore

ADD oms-to-elk-*.jar /opt/oms-to-elk/application.jar
ADD run.sh /opt/oms-to-elk/run.sh
RUN chmod +x /opt/oms-to-elk/run.sh; mkdir /opt/data

WORKDIR /opt/oms-to-elk
ENTRYPOINT ["/opt/oms-to-elk/run.sh"]
CMD ["start"]
