How to build
------------

mvn clean install


Library dependencies
--------------------

mvn dependency:tree | grep -v provided | grep -v test | sed 's/\[INFO\]//g' | sed 's/:compile//'

```
 Scanning for projects...
[WARNING] 
[WARNING] Some problems were encountered while building the effective model for com.payneteasy.reader-example:app-4-rxtx-qbang:jar:1.0-1-SNAPSHOT
[WARNING] 'build.plugins.plugin.version' for org.apache.maven.plugins:maven-compiler-plugin is missing. @ line 136, column 21
[WARNING] 
[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
[WARNING] 
[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
[WARNING] 
                                                                         
 ------------------------------------------------------------------------
 Building reader-example app-2 1.0-1-SNAPSHOT
 ------------------------------------------------------------------------
 
 --- maven-dependency-plugin:2.8:tree (default-cli) @ app-4-rxtx-qbang ---
 com.payneteasy.reader-example:app-4-rxtx-qbang:jar:1.0-1-SNAPSHOT
 +- org.slf4j:slf4j-api:jar:1.6.4
 +- org.slf4j:jcl-over-slf4j:jar:1.6.4
 +- org.slf4j:log4j-over-slf4j:jar:1.6.4
 +- ch.qos.logback:logback-classic:jar:0.9.29
 +- ch.qos.logback:logback-core:jar:0.9.29
 +- com.payneteasy.android.reader:lib:jar:1.4-28-SNAPSHOT
 |  +- com.payneteasy.android.reader:api:jar:1.4-28-SNAPSHOT
 |  |  \- com.squareup.okhttp:okhttp-urlconnection:jar:2.0.0
 |  +- com.payneteasy.paynet:paynet-remote-api-client:jar:3.25.07-44-SNAPSHOT-android
 |  +- com.payneteasy.paynet:paynet-remote-api:jar:3.25.07-44-SNAPSHOT-android
 |  +- org.scribe:scribe:jar:1.3.2
 |  +- com.squareup.okhttp:okhttp:jar:2.0.0
 |  |  \- com.squareup.okio:okio:jar:1.0.0
 |  \- com.google.code.gson:gson:jar:2.2.4
 |  +- commons-logging:commons-logging:jar:1.1.1
 |  +- org.apache.httpcomponents:httpclient:jar:4.0.1
 +- com.payneteasy.android.reader.readers:readers-common-rxtx-qbang:jar:1.4-28-SNAPSHOT
 |  +- com.payneteasy.android.reader.readers:readers-common-rxtx-api:jar:1.4-28-SNAPSHOT
 |  \- org.rxtx:rxtx:jar:2.1.7
 +- com.payneteasy.android.reader.readers:readers-miura-rxtx:jar:1.4-28-SNAPSHOT
 |  +- commons-codec:commons-codec:jar:1.3
 |  +- org.apache.httpcomponents:httpcore:jar:4.0.1
 |  \- com.payneteasy.android.reader.readers:readers-miura:jar:1.4-28-SNAPSHOT
 |     +- com.payneteasy.android.reader.readers:readers-common-bluetooth:jar:1.4-28-SNAPSHOT
 |     \- com.payneteasy:ber-tlv:jar:1.0-5
 ------------------------------------------------------------------------
 BUILD SUCCESS
 ------------------------------------------------------------------------
 Total time: 0.686s
 Finished at: Tue Oct 20 21:21:13 MSK 2015
 Final Memory: 9M/304M
 ------------------------------------------------------------------------

```

