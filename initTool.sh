#!/bin/sh

rm -r salesforce-manifest-generator
gradle build
unzip build/distributions/salesforce-manifest-generator.zip
cp credentials.json salesforce-manifest-generator/bin
salesforce-manifest-generator/bin/salesforce-manifest-generator
ls
