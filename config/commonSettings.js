'use strict'

const getEnv = require('kth-node-configuration').getEnv
const packageFile = require('../package.json')

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
  serviceBusConnectionString: getEnv('SBUS_ENDPOINT', 'sbus-endpoint-undefined'),
  schedule: getEnv('SCHEDULE', '* * * * *')
}
