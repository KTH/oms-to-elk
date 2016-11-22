'use strict';

const config = require('./server/init/configuration');
const azure = require('azure-sb');
const server = require('./server/server');
const schedule = require('node-schedule');
const log = require('kth-node-log');

log.info("Node env %s", process.env.NODE_ENV);

server.init();
server.start();

const sbService = azure.createServiceBusService(config.full.serviceBusConnectionString);

/*
 * Fetch statistics form service bus and store them in server.
 */
schedule.scheduleJob(config.full.schedule, function() {
    sbService.listQueues(function (err, res) {
        if (err) {
            log.error('Failed to list queues: %s: %s', err.code, err.detail);
            server.queues = null;
        } else {
            var queues = [];

            for (var i = 0; i < res.length; i++) {
                var queue = {};
                queue.name = res[i].QueueName;
                queue.size = parseInt(res[i].SizeInBytes);
                queue.max = parseInt(res[i].MaxSizeInMegabytes);
                queue.percent = queue.size / (queue.max * 1024 * 1000);
                queue.messages = parseInt(res[i].CountDetails['d2p1:ActiveMessageCount']);
                queue.dead_messages = parseInt(res[i].CountDetails['d2p1:DeadLetterMessageCount']);
                queues.push(queue);
            }
            server.queues = queues;
        }
    });
});

/*
 * Print statistics to log.
 */
schedule.scheduleJob(config.full.schedule, function() {
    var queues = server.queues;
    
    if (queues) {
        for (var i = 0; i < queues.length; i++) {
            var queue = queues[i];
            var jsonLog = {};
            jsonLog.Type = "ServiceBusQueueStatistics";
            jsonLog.QueueName = queue.name;
            jsonLog.QueueSizeInBytes = queue.size;
            jsonLog.QueueMazQueueSizeInMb = queue.max;
            jsonLog.QueuePercentUsed = queue.percent;
            jsonLog.QueueMessages = queue.messages;
            jsonLog.QueueDeadLetters = queue.dead_messages;
            process.stdout.write(JSON.stringify(jsonLog) + '\n');
        }
    } else {
        log.warn("No information");
    }
});
