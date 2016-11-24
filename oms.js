'use strict';

const config = require('./server/init/configuration');
const armclient = require('armclient');
const log = require('kth-node-log');
const logstash = require('./logstash');
const FiFoCache = require('fifo-cache');

var cache = initFifoCache();
var timestamp = initStartDate();

const omsclient = armclient({
  subscriptionId: config.full.subscriptionId,
  auth: armclient.clientCredentials({
    tenantId: config.full.tenantId,
    clientId: config.full.clientId,
    clientSecret: config.full.clientKey
  })
});

module.exports = {
    getSavedQuery: getSavedQuery,
    forwardLogEntriesToELK: forwardLogEntriesToELK
}

/*
 * Fetch the query string for a saved query and store it in the server.
 */
function getSavedQuery(server) {
    omsclient.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
      .get('/workspaces/' + config.full.workspace + '/savedSearches/' + config.full.savedSearch, { 'api-version': '2015-03-20' })
      .then(function(res) {
        if (server.query != res.body.properties.Query) {
            server.query = res.body.properties.Query;
            log.info("Setting query from OMS: '%s'", server.query);
        }
      })
      .catch((err) => {
        log.error("Unable to get search string for saved query '%s' in OMS: %s", config.full.savedSearch, err.details.error.message);
      })
}

/*
 * Forward the log entries matching query stored in server.
 */
function forwardLogEntriesToELK(query) {
    if (! query) {
        log.warn("Query not initialized.");
        return;
    }

    var apiQuery = {
        top: config.full.batchSize,
        Query: query,
        start: timestamp.toISOString(),
        end: new Date().toISOString()
    };

    omsclient.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
        .post('/workspaces/' + config.full.workspace + '/search', { 'api-version': '2015-03-20' }, apiQuery)
        .then(readEntries)
        .then(forwardEntries)
        .catch((err) => {
            log.error("Failed to retrieve log entries: %s", err)
        })
}

function updateTimestamp(d) {
    if (timestamp instanceof Date) {
        timestamp = new Date(Math.max(timestamp, d));
    } else {
        timestamp = d;
    }
    log.trace("Timestamp: %s", timestamp.toISOString());
}

function readEntries(res) {
    log.debug("got %d log entries", res.body.value.length);
    return res.body.value;
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
    } else {
        log.debug("cache hit for id: %s", entry.id);
    }
    return entry;
}

function initStartDate() {
    var timestamp;

//fs.readFileSync(config.full.logstashCertificatePath, {encoding: 'utf-8'})

    if (config.full.startDate) {
        timestamp = new Date(config.full.startDate);
    } else {
        timestamp = new Date();
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