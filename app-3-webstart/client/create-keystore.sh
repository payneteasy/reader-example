# keytool -genkey -alias alias-name -keystore store.ks

# open Java control panel
# select "Security" tab
# click "manage Certificates..." button
# select "Signer CA" option in the "Certificate type" combo-box
# import your CA's certificate
keytool -importkeystore -srckeystore store.ks -srcstoretype JKS -deststoretype PKCS12 -destkeystore store.p12
