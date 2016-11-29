'use-strict';

const config = require('./server/init/configuration');
const log = require('kth-node-log');
const lumberjack = require('lumberjack-protocol');
const url = require('url');
const fs = require('fs');

module.exports = {
    forward: forwardLogEntry,
    setServer: setServer
};

var server;

function setServer(srv) {
    server = srv;
}


const logstashHost = url.parse(config.full.logstashServer);
const connectionOptions = {
    host: logstashHost.hostname,
    port: logstashHost.port,
//    rejectUnauthorized: false,
    ca: [fs.readFileSync(config.full.logstashCaPath, {encoding: 'utf-8'})],
    cert: [fs.readFileSync(config.full.logstashCertificatePath, {encoding: 'utf-8'})],
    key: [fs.readFileSync(config.full.logstashKeyPath, {encoding: 'utf-8'})]
};

const client = lumberjack.client(connectionOptions, {maxQueueSize: 100});
client.on('connect', () => {
    if (config.full.keepAlive > 0) {
        log.debug("Setting keep alive: %d", config.full.keepAlive);
        // This is horrible, but we have to do it. Love Node.js and JavaScript.
        client._socket._socket.setKeepAlive(true, config.full.keepAlive);
    }
    log.info('Connected to logstash server');
});
client.on('disconnect', (err) => {
    log.warn('Disconnected from logstash server: %s', err);
});
client.on('dropped', (count) => {
    log.error('Logstash client has dropped %d messages', count);
    server.error = 'Logstash client has dropped '+ count + ' messages';
});

function forwardLogEntry(entry) {
    // ELK doesn't approve of the OMS meta data.'
    delete entry["__metadata"];
    client.writeDataFrame({"host": entry.hostname_s, "line": JSON.stringify(entry)});
    return entry;
}
