'use strict';

const config = require('./server/init/configuration');
const armclient = require('armclient');
const log = require('kth-node-log');
const logstash = require('./logstash');
const FiFoCache = require('fifo-cache');

var cache = new FiFoCache({
    max: config.full.cacheSize
});

var timestamp = {};
timestamp = new Date('05 October 2011 14:48 UTC');

const client = armclient({
  subscriptionId: config.full.subscriptionId,
  auth: armclient.clientCredentials({
    tenantId: config.full.tenantId,
    clientId: config.full.clientId,
    clientSecret: config.full.clientKey
  })
});

module.exports = {
    getSavedQuery: getSavedQuery,
    getLogEntries: getLogEntries
}

/*
 * Fetch the query string for a saved query and store it in the server.
 */
function getSavedQuery(server) {
    client.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
      .get('/workspaces/' + config.full.workspace + '/savedSearches/' + config.full.savedSearch, { 'api-version': '2015-03-20' })
      .then(function(res) {
        server.query = res.body.properties.Query;
        log.debug("Got query from OMS: '%s'", server.query);
      })
      .catch((err) => {
        log.error("Unable to get search string for saved query '%s' in OMS: %s", config.full.savedSearch, err.details.error.message);
    });
}

/*
 * Fetch the log entries matching query stored in server.
 */
function getLogEntries(server) {
    var query = server.query;

    if (! query) {
        log.warn("Query not initialized yet.");
        return;
    }

    var apiQuery = {
        top: config.full.batchSize,
        Query: query,
        start: timestamp.toISOString(),
        end: new Date().toISOString()
    };

    console.log(apiQuery);

    client.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
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