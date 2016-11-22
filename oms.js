'use strict';

const config = require('./server/init/configuration');
const tokens = require('/Users/fjo/.azure/accessTokens.json');
const armclient = require('armclient');
const log = require('kth-node-log');

const client = armclient({  
  subscriptionId: config.full.subscriptionId,
  auth: armclient.tokenCredentials({
    accessToken: tokens[0].accessToken
  })
});

module.exports = {
    getSavedQuery: getSavedQuery,
    getLogEntries: getLogEntries
}

function getSavedQuery(server) {
    client.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
      .get('/workspaces/' + config.full.workspace + '/savedSearches/' + config.full.savedSearch, { 'api-version': '2015-03-20' })
      .then((res) => {
        server.query = res.body.properties.Query;
        log.debug("Got query from OMS: '%s'", server.query);
      })
      .catch((err) => {
        log.error("Unable to get search string for saved query '%s' in OMS: %s", config.full.savedSearch, err);
    });
}

function getLogEntries(server) {
    var query = server.query;
    
    if (query) {
        var apiQuery = {
            top: 150,
            Query: query
        };

        client.provider(config.full.resourceGroup, 'Microsoft.OperationalInsights')
          .post('/workspaces/' + config.full.workspace + '/search', { 'api-version': '2015-03-20' }, apiQuery)
          .then((res) => {
            var logs = res.body.value;
            for (var i = 0; i < logs.length; i++) {
                console.log(logs[i].__metadata.TimeGenerated);
            }
          })
          .catch((err) => {
            log.error(err);
          });
    } else {
        log.warn("Query not initialized yet.");
    }
}