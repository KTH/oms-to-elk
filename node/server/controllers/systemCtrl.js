'use strict'

const packageFile = require('../../package.json')
const paths = require('../init/routing/paths')

/**
 * System controller for functions such as about and monitor.
 * Avoid making changes here in sub-projects.
 */
module.exports = {
  monitor: getMonitor,
  about: getAbout,
  robotsTxt: getRobotsTxt,
  paths: getPaths,
}

/**
 * GET /_about
 * About page
 */
function getAbout (req, res) {
  res.render('system/about', {
    appName: packageFile.name,
    appVersion: packageFile.version,
    appDescription: packageFile.description,
    monitorUri: paths.system.monitor.uri,
    robotsUri: paths.system.robots.uri
  })
}

function isOk (app) {
  if (app.error) {
    return false;
  }
  return true;
}

/**
 * GET /_monitor
 * Monitor page
 */
function getMonitor (req, res) {
  res.type('text').render('system/monitor', {
    test: isOk(req.app) ? 'OK' : 'ERROR',
    error: req.app.error,
    total: req.app.counters.total,
    timestamp: req.app.timestamp
  })
}

/**
 * GET /robots.txt
 * Robots.txt page
 */
function getRobotsTxt (req, res) {
  res.type('text').render('system/robots')
}

/**
 * GET /_paths
 * Return all paths for the system
 */
function getPaths (req, res) {
  res.json(paths)
}
