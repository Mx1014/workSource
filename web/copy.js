#!/usr/bin/env node

var shelljs = require('shelljs')
var path = require('path')

var cpy = path.join(__dirname, './node_modules/cpy-cli/cli.js')

shelljs.exec(cpy + ' ./source/shared/fonts/* ./dist/fonts')
shelljs.exec(cpy + ' ./source/shared/style/* ./dist/css')
shelljs.exec(cpy + ' ./source/assets/* ./dist/images')

