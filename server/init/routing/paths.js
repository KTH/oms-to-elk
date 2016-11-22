'use strict'

/**
 * Lists all paths (routes) with corresponding parameter(s) and method.
 * TIP: If you are getting a 500 response and the page keeps loading, check
 * the ordering of the routes in the affected files (routes/* and this).
 */

const routing = require('../../lib/routing')

module.exports = {
  system: {
    monitor: {
      uri: routing.prefix('/_monitor'),
      method: 'GET'
    },

    about: {
      uri: routing.prefix('/_about'),
      method: 'GET'
    },

    robots: {
      uri: '/robots.txt',
      method: 'GET'
    },

    paths: {
      uri: routing.prefix('/_paths'),
      method: 'GET'
    }
  }
}
