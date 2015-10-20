#!/usr/bin/env bash
set -eux

mvn clean install
mvn assembly:single
