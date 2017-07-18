# OMS to Logstash forwarder

A tool to forward logs from OMS to Logstash.

## Configuration

The application starts with the class path `/run/secrets:/opt/data:/opt/oms-to-elk`, allowing
for a number of options on how to configure the application.

1. You can use environment variables, which is used to create properties files in /opt/oms-to-elk.
1. You can instead choose to use properties in either /opt/data, which can be made a volume
   mount to some shared area.
1. Or you can use properties files in /run/secrets, using the new mechanism for secrets in Docker
   1.13.

The logstash keystore is also looked after on the class path and can be put in either of these
locations.

If you use a property file, it has to contain settings for all variables in the corresponding
section, but you can mix use of environment variables for one section, and properties for some
other. In particular, you may want to use a property file for the Azure settings,
azure.properties, which will be containing a lot of secrets.


### Environment configuration

Azure settings, corresponding properties are found in `azure.properties`.

| Environment | Property | Default | Description |
|-------------|----------|---------|-------------|
| AZURE_SUBSCRIPTION_ID | subscription | | The UUID of your Azure subscription. Looks like xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx |
| AZURE_RESOURCE_GROUP | resource_group | | The name of your resource group. |
| AZURE_WORKSPACE | oms_workspace | | The name of your OMS workspace. |
| AZURE_TENANTID | tenantId | | The UUID of the "tenant" for Oauth client authentication to Azure. |
| AZURE_CLIENTID | clientId | | The UUID of the oms-to-elk client to authenticate to Azure with. |
| AZURE_CLIENTKEY | clientKey | | The key corresponding to the UUID above. |

Logstash settings, corresponding properties are found in `logstash.properties`.

| Environment | Property | Default | Description |
|-------------|----------|---------|-------------|
| LOGSTASH_SERVER | server | | The name of the logstash host |
| LOGSTASH_PORT | port | | The logstash port number to use |
| LOGSTASH_KEYSTORE | keystore | oms-to-elk.keystore | The name of the keystore file to look for, not a path

Oms-to-elk general settings, corresponding properties are found in `oms-to-elk.properties`.

| Environment | Property | Default | Description |
|-------------|----------|---------|-------------|
| OMS_ELK_SAVED_QUERY | saved_query | oms-to-elk:Default | The category and name of the keystore file to look for, not a path |


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

The client needs a certificate to authenticate to logstash. These have to
be stored into a keystore. There is plenty of information on the webb on
how to acheive this given different certificates. The keystore is pointed
out by LOGSTASH_KEYSTORE (default `oms-to-elk.keystore` see above). This
keystore will be sought on the class path, making it possible to supply
this given a volume mount for /opt/data, or in /run/secrets via the 
mechanism available since Docker 1.13.

Chances are that you will also have to add the logstash server certificate
to the keystore and trust it. It seems difficult to get logstash to provide
a proper chain.

The client stores a timestamp for the last seen log entry, in /opt/data/timestamp.
If you want to persist it, e.g. to make it available on many hosts in a
swarm environment, make a volume mount for it. The file will be created
if it does not exist.

## Development

The client code for operational insights (OMS) is autogenerated from the
swagger definition (https://github.com/Azure/azure-rest-api-specs) 
using autorest (https://github.com/Azure/autorest). Use the most recent
version of autorest. Generally that means installing wuth nuget.

### Getting with nuget on Mac

Nuget can be run using mono on Mac OS X and it all can be retrieved with
Homebrew (http://brew.sh/)

```
brew install mono nuget
nuget install autorest
```

The autorest executable is then found in `AutoRest.0.17.3/tools/`

### Using Autorest to generate sources.

```
mono AutoRest.exe \
    --Namespace se.kth.integral.oms \
    -Input .../azure-rest-api-specs/arm-operationalinsights/2015-03-20/swagger/OperationalInsights.json \
    -CodeGenerator Azure.Java \
    -Modeler Swagger
```

The code is then found in the `Generated` folder. Move it to the destination.