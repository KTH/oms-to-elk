'use-strict';

const config = require('./server/init/configuration');
const log = require('kth-node-log');
const lumberjack = require('lumberjack-protocol');
const url = require('url');
const fs = require('fs');

var logstashHost = url.parse(config.full.logstashServer);

// var connectionOptions = {
//     host: logstashHost.hostname,
//     port: logstashHost.port,
//     ca: [fs.readFileSync(config.full.logstashCertificatePath, {encoding: 'utf-8'})]
// };

// var client = lumberjack.client(connectionOptions, {maxQueueSize: 500});

//client.writeDataFrame({"line": "Hello World!"});

