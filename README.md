# OMS to Logstash forwarder

A tool to forward logs from OMS to Logstash.

## Configuration

### OMS

The node client uses the "client credentials type" Oauth2 authentication when 
connecting to OMS. So you need to set up an "App" in Azure AD. You need values
for the _TenantID_, _ClientID_ and _Client key_ you get when setting 
up this container. I'm told there are instructions on how to create these.
I really have no idea. 

Then, you have to give the application access to your OMS workspace. This however
is easy. You go to your Log Analytics area in the new Azure portal and give the
application read access under Access control (IAM). You need the OMS 
_workspace name_ when setting up the container.
You also need the Azure _subscription id_ and the _resource group id_ where
the OMS workspace is located.

### Logstash

You have to generate a _certificate_ for the client as per usual for authentication.
In addition to this, there are a number of tweeks you want to add to your logstash
configuration. A complete, if trivial, example used for testing with the 
sebp/elk:es241_l240_k461 docker image can be found in
`elk/conf.d/01-lumberjack-input.conf`

The under lying node tls library assumes a certificate and corresponding key file
both in PEM format.

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
but the duplicate detection cache is not persisted, so there is a chance items will
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

### Certificates and persistence of timestamp

The client needs certificates to authenticate to logstash. The path and name 
of the certificate is defined by LOGSTASH_CERT_PATH (see below), and the 
corresponding key by LOGSTASH_KEY_PATH. You can provide this by a volume mount
of some place containing the file.

Currently there is assumed a ca.crt file used to verify the server identity,
defined by LOGSTASH_CA_PATH. However I've not quite figured that one out yet,
so server verification is disabled. The file is still expected to exist though.

The client stores a timestamp for the last seen log entry, the path and name
is defined by OMS_ELK_TIMESTAMP_PATH. If you want to persist it, e.g. to make
it available on many hosts in a swarm environment, make a volume mount for it.
The file will be created if it does not exist.


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
| OMS_ELK_START_DATE | none | If set, and no stored timestamp is found, start from this timestamp in ISO 8601 format. | 
| LOGGING_LEVEL | info | Logging level. More information will be printed if set to debug. |
| OMS_SAVED_QUERY | oms-to-elk\|Default | The name of the saved query in OMS to use. |
| OMS_OMS_SAVED_SEARCH_SCHEDULE | */20 * * * * * | When to check if query changed, default every 20 minutes. |
| LOG_QUERY_SCHEDULE | */20 * * * * | When to check for new log entries, default every 20 seconds. |
| OMS_STATISTICS_SCHEDULE | * * * * * | When to print a statistics log, default every minute. |
| LOGSTASH_CERT_PATH | /opt/data/client.crt | The path to the PEM certificate to authenticate to logstash with. |
| LOGSTASH_KEY_PATH | /opt/data/client.key | The path to the PEM key for the certificate file. |
| LOGSTASH_CA_PATH | /opt/data/ca.crt | The path to the PEM ca file to use when verifying server. |
| OMS_ELK_TIMESTAMP_PATH | /opt/data/timestamp.json | The path to the persisted timestamp. |
| OMS_QUERY_BATCH_SIZE | 200 | The maximum number of items to fetch from OMS in one poll. |
| OMS_ELK_CACHE_SIZE | 1000 | How many log entry ids we keep track of. |
| OMS_ELK_MAX_QUEUE_SIZE | 1000 | How many messages to keep in memory while waiting for logstash server before dropping messages. |
| OMS_ELK_BACK_TICK | 10000 | A number of milliseconds we remove from timestamp in each poll in case OMS indexed some entry late. |
| OMS_ELK_KEEPALIVE | 60000 | Set keep-alive in ms on logstash socket. A value of 0 disables keep-alive. |

For details on format for scheduling options, see https://www.npmjs.com/package/node-schedule

## Running

Given a file environment containing environment variables as mentioned above, the image can be
run with docker run as in this example. There is a template available in environment.in.

```
docker run --env-file=environment --net=<some net> -v /some/path:/opt/data --kthse/oms-to-elk:latest
```

Given that the certificate files as mentioned above, as well as the timestamp file, are 
kept in the read-write directory /some/path.

## Development

### Running an ELK docker container

In the `elk` folder there is a helper script and configuration to run a docker
image with logstash and json codec enabled on input for development.