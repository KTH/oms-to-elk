'use strict';

const config = require('./server/init/configuration');
const server = require('./server/server');
const schedule = require('node-schedule');
const log = require('kth-node-log');

const oms = require('./oms');

log.info("Node env %s", process.env.NODE_ENV);

server.init();
server.start();

oms.getSavedQuery(server);

/*
 * Fetch the saved query from server.
 */
schedule.scheduleJob(config.full.savedSearchSchedule, function() {
    oms.getSavedQuery(server);
});

/*
 * Fetch the log entries from the server.
 */
schedule.scheduleJob(config.full.logQuerySchedule, function() {
    oms.getLogEntries(server);
});
