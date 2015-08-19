#!/usr/bin/env bash
set -eux

(cd .. && mvn --non-recursive clean install )
mvn clean install
mvn assembly:single
