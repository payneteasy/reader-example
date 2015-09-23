#!/usr/bin/env bash
set -eux

(cd .. && mvn clean install )
mvn assembly:single
