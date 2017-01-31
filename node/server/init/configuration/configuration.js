'use strict'

const configurator = require('kth-node-configuration')

const config = configurator({
  defaults: require('../../../config/commonSettings')
})

module.exports = {
  full: config.full(),
  secure: config.secure(),
  safe: config.safe(),
  env: config.env()
}
