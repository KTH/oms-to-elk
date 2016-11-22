# KTH Integral monitor

A simple monitor app for KTH Integral.

Monitors Service Bus queues and there state.
It periodically, according to a configurable schedule, prints JSON-formatted
data on stdout like this:

```
{"Type":"ServiceBusQueueStatistics","QueueName":"ug-canvas-ref","QueueSizeInBytes":0,"QueueMazQueueSizeInMb":5120,"QueuePercentUsed":0,"QueueMessages":0,"QueueDeadLetters":0}
{"Type":"ServiceBusQueueStatistics","QueueName":"ug-propagator","QueueSizeInBytes":0,"QueueMazQueueSizeInMb":16384,"QueuePercentUsed":0,"QueueMessages":0,"QueueDeadLetters":0}
{"Type":"ServiceBusQueueStatistics","QueueName":"ladok3-feed","QueueSizeInBytes":0,"QueueMazQueueSizeInMb":81920,"QueuePercentUsed":0,"QueueMessages":0,"QueueDeadLetters":0}
{"Type":"ServiceBusQueueStatistics","QueueName":"ug-canvas","QueueSizeInBytes":76593,"QueueMazQueueSizeInMb":5120,"QueuePercentUsed":0.000014608955383300781,"QueueMessages":0,"QueueDeadLetters":106}
```

This data can then be collected with and monitored with logging analyzis tools.

In addition to this the app also provides a simple _monitor-page on /monitor/_monitor on port 3001.
Currently the monitor page will show errors if there are messages in the dead letter queue for any
of the queues found in the Service Bus name space.

## Configuration

| Variable | Description |
|----------|-------------|
| SBUS_ENDPOINT | Service Bus endpoint connection URI as found in the portal |
| SCHEDULE | A cron-like schedule, default `* * * * *` for every minute. |

The SBUS_ENDPOINT used must have manage privileges for the Service Bus name space (top level).

For details on format for SCHEDULE, see https://www.npmjs.com/package/node-schedule

## Running

Given a file environment containing environment variables as mentioned above, the image can be run with docker run as in this example.
There is a template available in environment.in.

```
docker run --env-file=environment kthse/integral-monitor:latest
```
