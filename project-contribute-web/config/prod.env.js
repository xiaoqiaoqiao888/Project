'use strict'

module.exports = {
  NODE_ENV: '"production"',
  HOST_URL: process.argv[2] == "prod" ? '"http://172.21.101.116:9090"' : '"http://172.21.101.115:9090"'
}
