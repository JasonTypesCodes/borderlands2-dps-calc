
var jarPath = require('./node_modules/selenium-server-standalone-jar/index.js').path;

exports.config = {
  //seleniumServerJar: jarPath,
  seleniumAddress: 'http://localhost:4444/wd/hub',
  multiCapabilities: [
    {
      browserName: 'chrome'
    },
    {
      browserName: 'phantomjs'
    }
  ]
}