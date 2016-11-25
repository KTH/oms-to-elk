'use strict';

const config = require('./server/init/configuration');
const armclient = require('armclient');
const log = require('kth-node-log');
const logstash = require('./logstash');
const FiFoCache = require('fifo-cache');
const fs = require('fs');

var cache = initFifoCache();

const omsclient = armclient({
  subscriptionId: config.full.subscriptionId,
  auth: armclient.clientCredentials({
    tenantId: config.full.tenantId,
    clientId: config.full.clientId,
    clientSecret: config.full.clientSecret
  })
});

var server;

function setServer(srv) {
    server = srv;
    server.timestamp = initStartDate();
    logstash.setServer(server);
}

module.exports = {
    getSavedQuery: getSavedQuery,
    forwardLogEntriesToELK: forwardLogEntriesToELK,
    setServer: setServer
}

/*
 * Fetch the query string for a saved query and store it in the server.
 */
function getSavedQuery() {
    omsclient.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
      .get('/workspaces/' + config.full.omsWorkspace + '/savedSearches/' + config.full.savedSearch, { 'api-version': '2015-03-20' })
      .then(function(res) {
        if (server.query != res.body.properties.Query) {
            server.query = res.body.properties.Query;
            log.info("Setting query from OMS: '%s'", server.query);
        }
      })
      .catch((err) => {
        log.error(err, "Unable to get search string for saved query '%s' in OMS.", config.full.savedSearch);
      })
}

/*
 * Forward the log entries matching query stored in server.
 */
function forwardLogEntriesToELK() {
    var query = server.query;

    if (! query) {
        log.warn("Query not initialized.");
        return;
    }

    var apiQuery = {
        top: config.full.batchSize,
        Query: query,
        start: new Date(server.timestamp - config.full.backTick).toISOString(),
        end: new Date().toISOString()
    };

    omsclient.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
        .post('/workspaces/' + config.full.omsWorkspace + '/search', { 'api-version': '2015-03-20' }, apiQuery)
        .then(readEntries)
        .then(forwardEntries)
        .then(recordTimestamp)
        .catch((err) => {
            log.error(err, "Failed to retrieve log entries.")
        })
}

function updateTimestamp(d) {
    if (server.timestamp instanceof Date) {
        server.timestamp = new Date(Math.max(server.timestamp, d));
    } else {
        server.timestamp = d;
    }
    log.trace("Timestamp: %s", server.timestamp.toISOString());
}

function readEntries(res) {
    log.debug("got %d log entries", res.body.value.length);
    return res.body.value;
}

function recordTimestamp(res) {
    return new Promise(function(resolve, reject) {
        fs.writeFile(config.full.timestampFile, server.timestamp.toJSON(), 'utf-8', function(err) {
            if (err) reject(err);
            else resolve(res);
        });
    });
}

function forwardEntries(entries) {
    return Promise.all(entries.map(forwardEntry));
}

function forwardEntry(entry) {
    updateTimestamp(new Date(entry.TimeGenerated));

    if (! cache.get(entry.id)) {
        log.debug("forwarding id: %s", entry.id);
        logstash.forward(entry);
        cache.set(entry.id, {});
        server.counters.total += 1;
        server.counters.delta += 1;
    } else {
        log.debug("cache hit for id: %s", entry.id);
    }
    return entry;
}

function initStartDate() {
    var timestamp;

    try {
        timestamp = new Date(fs.readFileSync(config.full.timestampFile, {encoding: 'utf-8'}));
    } catch (err) {
        log.warn(err, "Unable to load initial date from file.")
        if (config.full.startDate) {
            log.info("Using initial date from environment: %s", config.full.startDate);
            timestamp = new Date(config.full.startDate);
        } else {
            log.info("Using current time as initial date.");
            timestamp = new Date();
        }
    }

    log.info("Start getting log entries from: %s", timestamp.toISOString());
    return timestamp;
}

function initFifoCache() {
    log.info("Using fifo cache size: %d", config.full.cacheSize);
    return new FiFoCache({
        max: config.full.cacheSize
    });
}