How to build without android sdk
--------------------------------

```
cd reader-example
mvn -N clean install
cd app-2
mvn clean install
```

Library dependencies
--------------------
mvn dependency:tree | grep -v provided | grep -v test | sed 's/\[INFO\]//g' | sed 's/:compile//'

```
com.payneteasy.reader-example:app-2:jar:1.0-1-SNAPSHOT
 +- org.slf4j:slf4j-api:jar:1.6.4
 +- org.slf4j:jcl-over-slf4j:jar:1.6.4
 +- org.slf4j:log4j-over-slf4j:jar:1.6.4
 +- ch.qos.logback:logback-classic:jar:0.9.29
 +- ch.qos.logback:logback-core:jar:0.9.29
 +- com.payneteasy.android.reader:lib:jar:1.4-27-SNAPSHOT
 |  +- com.payneteasy.android.reader:api:jar:1.4-27-SNAPSHOT
 |  |  \- com.squareup.okhttp:okhttp-urlconnection:jar:2.0.0
 |  +- com.payneteasy.paynet:paynet-remote-api-client:jar:3.25.07-44-SNAPSHOT-android
 |  +- com.payneteasy.paynet:paynet-remote-api:jar:3.25.07-44-SNAPSHOT-android
 |  +- org.scribe:scribe:jar:1.3.2
 |  +- com.squareup.okhttp:okhttp:jar:2.0.0
 |  |  \- com.squareup.okio:okio:jar:1.0.0
 |  \- com.google.code.gson:gson:jar:2.2.4
 |  +- commons-logging:commons-logging:jar:1.1.1
 |  +- org.apache.httpcomponents:httpclient:jar:4.0.1
 +- com.payneteasy.android.reader.readers:readers-miura-rxtx:jar:1.4-27-SNAPSHOT
 |  +- gnu.io.rxtx:rxtx-api:jar:2.2-stabilize-SNAPSHOT
 |  +- gnu.io.rxtx:rxtxSerial:jar:2.2-stabilize-SNAPSHOT
 |  |  \- gnu.io.rxtx:rxtxSerial-java:jar:2.2-stabilize-SNAPSHOT
 |  +- commons-codec:commons-codec:jar:1.3
 |  +- org.apache.httpcomponents:httpcore:jar:4.0.1
 |  \- com.payneteasy.android.reader.readers:readers-miura:jar:1.4-27-SNAPSHOT
 |     +- com.payneteasy.android.reader.readers:readers-common-bluetooth:jar:1.4-27-SNAPSHOT
 |     \- com.payneteasy:ber-tlv:jar:1.0-5
```
