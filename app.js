'use strict';

const config = require('./server/init/configuration');
const server = require('./server/server');
const schedule = require('node-schedule');
const log = require('kth-node-log');
const oms = require('./oms');

server.init();
server.start();

// Initialize.
oms.setServer(server);
oms.getSavedQuery();

server.status = "OK";
server.counters = {
    total: 0,
    delta: 0
}

/*
 * Fetch the saved query from server.
 */
schedule.scheduleJob(config.full.savedSearchSchedule, function() {
    oms.getSavedQuery();
});

/*
 * Fetch the log entries from the server.
 */
schedule.scheduleJob(config.full.logQuerySchedule, function() {
    oms.forwardLogEntriesToELK();
});

/*
 * Fetch the log entries from the server.
 */
schedule.scheduleJob(config.full.statisticsSchedule, function() {
    var jsonLog = {};
    
    jsonLog.Type = "OmsToElkStatistics";
    if (server.query) {
        jsonLog.Query = server.query;
    } else {
        jsonLog.Query = "Not intialized";
    }
    jsonLog.TotalMessages = server.counters.total;
    jsonLog.Messages = server.counters.delta;
    process.stdout.write(JSON.stringify(jsonLog) + '\n');
    server.counters.delta = 0;
});
