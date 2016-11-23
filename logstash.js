'use-strict';

const config = require('./server/init/configuration');
const log = require('kth-node-log');
const lumberjack = require('lumberjack-protocol');
const url = require('url');
const fs = require('fs');

module.exports = {
    forward: forwardLogEntry
}

const logstashHost = url.parse(config.full.logstashServer);
const connectionOptions = {
    host: logstashHost.hostname,
    port: logstashHost.port,
    ca: [fs.readFileSync(config.full.logstashCertificatePath, {encoding: 'utf-8'})],
    checkServerIdentity: function (servername, cert) {}
};

const client = lumberjack.client(connectionOptions, {maxQueueSize: 100});
client.on('connect', () => {
    log.info('Connected to logstash server');
});
client.on('disconnect', (err) => {
    log.warn('Disconnected from logstash server: %s', err)
});
client.on('dropped', (count) => {
    log.error('Logstash client has dropped %d messages', count)
});

//client.writeDataFrame({"line": "Hello World!"});

function forwardLogEntry(entry) {
    client.writeDataFrame({"host": entry.hostname_s, "line": JSON.stringify(entry)});
    return entry;
}
