
var jarPath = require('./node_modules/selenium-server-standalone-jar/index.js').path;

exports.config = {
  seleniumServerJar: jarPath,
  multiCapabilities: [
    {
      browserName: 'firefox'
    },
    {
      browserName: 'phantomjs'
    }
  ]
}