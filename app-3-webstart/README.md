How to use the mPOS SDK via Java Web Start
==========================================

The Sequest Diagram

.. figure:: webstart.png

Steps
-----

* Make a project

   mvn clean install

* Create your certificate, private key and put them to a key stores (JKS and P12).
  
   cd client && create-keystore.sh
  
* Install your certificate in Java

 1. open Java control panel
 2. select "Security" tab
 3. click "manage Certificates..." button
 4. select "Signer CA" option in the "Certificate type" combo-box
 5. import your CA's certificate
   
* Copy new mpos.jar to a web-server

   cd client && ./assemble.sh
   
* Assemble a webserver single jar archive

   cd server && ./assemble.sh
   
* Run a webserver

   java -jar server/target/server-1.0-1-SNAPSHOT-jar-with-dependencies.jar
   
* Open your browser at http://localhost:8080


