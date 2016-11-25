'use strict';

const getEnv = require('kth-node-configuration').getEnv;
const packageFile = require('../package.json');

module.exports = {
  proxyPrefixPath: {
    uri: '/monitor'
  },
  hostUrl: getEnv('SERVER_HOST_URL', 'http://localhost:3001'),
  port: getEnv('SERVER_PORT', 3001),
  log: {
    name: packageFile.name,
    app: packageFile.name,
    level: getEnv('LOGGING_LEVEL', 'info'),
    env: getEnv('NODE_ENV'),
    stream: process.stdout,
    console: {
      enabled: true,
      format: {
        outputMode: 'raw'
      }
    }
  },
  // Stuff with defaults that can be tweaked.
  savedSearch: getEnv('OMS_SAVED_QUERY', 'oms-to-elk|Default'),
  logQuerySchedule: getEnv('OMS_LOG_QUERY_SCHEDULE', '*/20 * * * * *'),
  savedSearchSchedule: getEnv('OMS_SAVED_SEARCH_SCHEDULE', '*/10 * * * *'),
  statisticsSchedule: getEnv('OMS_STATISTICS_SCHEDULE', '* * * * *'),
  logstashServer: getEnv('LOGSTASH_SERVER_URL', 'logstash://localhost:5000'),
  logstashCertificatePath: getEnv('LOGSTASH_CERT_PATH', './client.crt'),
  logstashKeyPath: getEnv('LOGSTASH_KEY_PATH', './client.key'),
  logstashCaPath: getEnv('LOGSTASH_CA_PATH', './ca.crt'),
  timestampFile: getEnv('OMS_ELK_TIMESTAMP_PATH', './timestamp.json'),
  maxQueueSize: getEnv('OMS_ELK_MAX_QUEUE_SIZE', 1000),
  batchSize: getEnv('OMS_QUERY_BATCH_SIZE', 200),
  cacheSize: getEnv('OMS_ELK_CACHE_SIZE', 1000),
  backTick: getEnv('OMS_ELK_BACK_TICK', 30000),
  // Truly optional without values.
  startDate: getEnv('OMS_ELK_START_DATE'),
  // Mandatory stuff that needs configuration.
  subscriptionId: getEnv('AZURE_SUBSCRIPTION_ID'),
  resourceGroup: getEnv('AZURE_RESOURCE_GROUP'),
  omsWorkspace: getEnv('OMS_WORKSPACE'),
  tenantId: getEnv('OMS_ELK_TENANTID'),
  clientId: getEnv('OMS_ELK_CLIENTID'),
  clientSecret: getEnv('OMS_ELK_CLIENTKEY')
};
