#!/bin/bash

npm install -g gulp phantomjs bower protractor

webdriver-manager update

npm install

bower install

gulp test
