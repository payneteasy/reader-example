#!/usr/bin/env bash
set -eux

(cd .. && mvn clean install )
mvn clean install
mvn assembly:single
cp target/client-1.0-1-SNAPSHOT-jar-with-dependencies.jar ../server/src/main/resources/web/mpos.jar

jarsigner -keystore store.ks \
    -storepass password \
    -keypass password \
     ../server/src/main/resources/web/mpos.jar \
     alias-name
