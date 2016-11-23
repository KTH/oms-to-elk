'use strict';

const getEnv = require('kth-node-configuration').getEnv;
const packageFile = require('../package.json');

module.exports = {
  // The proxy prefix path if the application is proxied. E.g /api/node
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
  subscriptionId: getEnv('AZURE_SUBSCRIPTION_ID', '2c39d62f-c399-4518-a841-4c1952136db5'),
  resourceGroup: getEnv('AZURE_RESOURCE_GROUP', 'UF-ITA-INTEGRATION'),
  workspace: getEnv('OMS_WORKSPACE', 'kth-integral'),
  savedSearch: getEnv('OMS_SAVED_QUERY', 'elk|BunyanWarn'),
  logQuerySchedule: getEnv('OMS_LOG_QUERY_SCHEDULE', '*/10 * * * * *'),
  savedSearchSchedule: getEnv('OMS_SAVED_SEARCH_SCHEDULE', '*/10 * * * *'),
  batchSize: getEnv('OMS_QUERY_BATCH_SIZE', 10),
  logstashServer: getEnv('LOGSTASH_SERVER_URL', 'logstash://localhost:5000'),
  logstashCertificatePath: getEnv('LOGSTASH_CERT_PATH', './logstash.crt'),
  tenantId: getEnv('OMS_ELK_TENANTID'),
  clientId: getEnv('OMS_ELK_CLIENTID'),
  clientKey: getEnv('OMS_ELK_CLIENTKEY')
};
