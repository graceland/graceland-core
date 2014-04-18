#!/bin/bash

echo "========================"
echo "Attempting to Publish..."
echo "========================"

TB=$TRAVIS_BRANCH
TP=$TRAVIS_PULL_REQUEST
TS=$TRAVIS_SECURE_ENV_VARS

echo "TRAVIS_BRANCH             = ${TRAVIS_BRANCH}"
echo "TRAVIS_PULL_REQUEST       = ${TRAVIS_PULL_REQUEST}"
echo "TRAVIS_SECURE_ENV_VARS    = ${TRAVIS_SECURE_ENV_VARS}"

if [[ $TB == "develop" && $TP == "false" && $TS == "true" ]]; then
    echo "== Deploying..."
    set -v
    mvn deploy --settings travis/settings.xml -DskipTests=true
fi
