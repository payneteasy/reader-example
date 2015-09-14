How to use the mPOS SDK via Java Web Start
==========================================

The Overall Process

![](webstart.png)

How to run this example
-----------------------

* Make a project

```bash
   mvn clean install
```

* Create your certificate, private key and put them to a key stores (JKS and P12).
  
```bash
   cd client && create-keystore.sh
```

* Install your certificate in Java

 1. open Java control panel
 2. select "Security" tab
 3. click "manage Certificates..." button
 4. select "Signer CA" option in the "Certificate type" combo-box
 5. import your CA's certificate
   
* Copy new mpos.jar to a web-server

```bash
 cd client && ./assemble.sh
```

* Assemble a webserver single jar archive

```bash
   cd server && ./assemble.sh
```

* Run a webserver

```bash
   java -jar server/target/server-1.0-1-SNAPSHOT-jar-with-dependencies.jar
```
   
* Open your browser at http://localhost:8080
