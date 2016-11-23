# OMS to Logstash forwarder

A tool to forward logs from OMS to Logstash.

## Configuration

### OMS authentication configuration

How to set up OMS.

### Logstash authentication configuration

How to set up Logstash.

### Environment configuration

| Variable | Description |
|----------|-------------|
| x | y |

## Running

Given a file environment containing environment variables as mentioned above, the image can be
run with docker run as in this example. There is a template available in environment.in.

```
docker run --env-file=environment kthse/oms-to-elk:latest
```
