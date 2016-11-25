# OMS to Logstash forwarder

A tool to forward logs from OMS to Logstash.

## Configuration

### OMS

How to set up OMS.

### Logstash

You have to generate a certificate for the client as per usual for authentication.
In addition to this, there are a number of tweeks you want to add to your logstash
configuration. A complete, if trivial, example used for testing with the 
sebp/elk:es241_l240_k461 docker image can be found in
`elk/conf.d/01-lumberjack-input.conf`

#### input

You have to set the json codec on the input used with this forwarder.

```
input {
  lumberjack {
    ...
    codec => "json"
  }
}
```

#### filter

Optional but recommended.
In order to set the date for logged items to the timestamp the item was logged in OMS, 
rather than the time the item is forwarded to ELK, you have to add a "date" filter to
extract the TimeGenerated value from OMS into @timestamp used by ELK.

```
filter {
  date {
    match => [ "TimeGenerated", "ISO8601" ]
  }
}
```

If you choose, you can do all kinds of other magic with filters,  

#### output

Optional but recommended. The forwarder tries to avoid sending items multiple times,
but the duplicate detection cache is not persisted, so there is a change items will
be forwarded again on restarts, causing duplicate entries in ELK.

To avoid this you can use the UUID set by OMS for each entry as the document id used 
by elastic in the output configuration.

```
output {
  if [id] {
    elasticsearch {
      ...
      document_id => "%{id}"
    }
  } else {
      ...
  }
}
```

## Container configuration

### Environment configuration

Required settings.

| Variable | Description |
|----------|-------------|
| AZURE_SUBSCRIPTION_ID | The UUID of your Azure subscription. Looks like xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx |
| AZURE_RESOURCE_GROUP | The name of your resource group. |
| OMS_WORKSPACE | The name of your OMS workspace. |
| OMS_ELK_TENANTID | The UUID of the "tenant" for Oauth client authentication to Azure. |
| OMS_ELK_CLIENTID | The UUID of the oms-to-elk client to authenticate to Azure with. |
| OMS_ELK_CLIENTKEY | The key corresponding to the UUID above. |
| LOGSTASH_SERVER_URL | A URI to the logstash host. Protocol is ignored, only host and port information is used. E.g. logstash://localhost:5000 |

Optional settings, mainly tuning.

| Variable | Default | Description |
|----------|---------|-------------|
| OMS_SAVED_QUERY | oms-to-elk\|Default | The name of the saved query in OMS to use. |
| OMS_OMS_SAVED_SEARCH_SCHEDULE | */20 * * * * * | When to check if query changed, default every 20 minutes. |
| LOG_QUERY_SCHEDULE | */20 * * * * | When to check for new log entries, default every 20 seconds. |
| OMS_STATISTICS_SCHEDULE | * * * * * | When to print a statistics log, default every minute. |
| LOGSTASH_CERT_PATH | /opt/data/logstash.crt | The path to the certificate to authenticate to logstash with. |
| OMS_ELK_TIMESTAMP_PATH | /opt/data/timestamp.json | The path to the persisted timestamp. |
| OMS_QUERY_BATCH_SIZE | 200 | The maximum number of items to fetch from OMS in one poll. |
| OMS_ELK_CACHE_SIZE | 1000 | How many log entry ids we keep track of. |
| OMS_ELK_BACK_TICK | 30000 | A number of milliseconds we remove from timestamp in each poll in case OMS indexed some entry late. |

For details on format for scheduling options, see https://www.npmjs.com/package/node-schedule

## Running

Given a file environment containing environment variables as mentioned above, the image can be
run with docker run as in this example. There is a template available in environment.in.

```
docker run --env-file=environment kthse/oms-to-elk:latest
```

## Development

### Running an ELK docker container

In the `elk` folder there is a helper script and configuration to run a docker
image with  logstash and json codec enabled on input for development.