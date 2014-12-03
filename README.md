mPOS SDK for Android
============================

SDK provides fast, easy integration with mPOS readers in mobile apps.

Supported Readers
-----------------
### Production:
* Miura Shutle, Miura M007, Miura M010
* OTI Saturn 6500

### Deprecated from the 1st of January 2015 
* GD Seed: Integrated with the official SDK 
* GD Seed: Own SDK. With memory and performance optimization.
* ID Tech Unimag II: Integrated with the official SDK 
* ID Tech Unimag II: Own SDK. Memory and performance optimization. Extends supported phone models.
* Bluebamboo P25

### Evaluated
* Datecs DRD50, DRD10
* Datecs MPED400, Bluepad 50

### Only for integration or test
* TEST - emulates Miura Shuttle events. You can use it on emulators.


Instructions
------------

The SDK includes a maven repository - http://paynet-qa.clubber.me/reader/maven/

We'll walk you through integration and usage.

### Sign up for PaynetEasy account 

* You'll need to contact to payneteasy.com for merchant login.

### Requirements

*   Supports target deployment of Android from 2.2.

### Setup

Add the repository to your pom.xml
```xml
<repository>
    id>reader-repo</id>
    <name>reader repo</name>
    <url>http://paynet-qa.clubber.me/reader/maven</url>
</repository>
```
### For Bluetooth Readers

Add to your AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.BLUETOOTH"/>
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
```

### For Audio Jack Readers
Add to your AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
```

### Sample Code

Implement the IReaderPresenter interface
```java

    @Override
    public ProcessingContinuation onCard(BankCard bankCard) {

        setStatus("onCard: %s", bankCard);

        return ProcessingContinuation.Builder
                .startSaleOnline()
                .processingBaseUrl  ( Config.SERVER_BASE_URL)
                .merchantLogin      ( Config.MERCHANT_LOGIN )
                .merchantControlKey ( Config.MERCHANT_KEY   )
                .merchantEndPointId ( Config.END_POINT_ID   )
                .orderDescription   ( "test description"    )
                .orderInvoiceNumber ( "invoice-"+System.currentTimeMillis())
                .orderMerchantData  ( "custom merchant data for a internal use")
                .customerPhone      ( "+7 499 918-64-41"    )
                .customerEmail      ( "info@payneteasy.com" )
                .customerCountry    ( "RUS"                 )
                .listener(new IProcessingStageListener() {
                    @Override
                    public void onStageChanged(ProcessingStageEvent aEvent) {
                        setStatus("processing: %s", aEvent);
                    }
                })
                .build();

    }

    @Override
    public void onReaderSerialNumber(String aKsn) {
        setStatus("onReaderSerialNumber: %s", aKsn);
    }

    @Override
    public void cardReaderStateChanged(CardReaderEvent cardReaderEvent) {
        setStatus("cardReaderStateChanged: %s", cardReaderEvent);
    }

    @Override
    public void onCardError(CardError cardError) {
        setStatus("onCardError: %s", cardError);
    }

    @Override
    public void onReaderNotSupported(CardReaderProblem aProblem) {
        setStatus("onReaderNotSupported: %s", aProblem);
    }

    @Override
    public void onAudioData(short[] shorts, int i) {
        // for visualization
    }

    @Override
    public ConfigurationContinuation onConfiguration() {
        return new ConfigurationContinuation.Builder()
                .configDir              ( new File(activity.getFilesDir(), "miura-config"))
                .configurationBaseUrl   ( Config.SERVER_CONFIG_URL  )
                .merchantLogin          ( Config.MERCHANT_LOGIN     )
                .merchantControlKey     ( Config.MERCHANT_KEY       )
                .merchantEndPointId     ( Config.END_POINT_ID       )
                .build();
    }
```

Note: Fill the Config.MERCHANT_LOGIN, Config.MERCHANT_KEY, Config.END_POINT_ID with your own values.

Starts Reader Manager

```java
CardReaderInfo cardReader = CardReaderInfo.TEST;
BigDecimal amount = new BigDecimal(1);
String currency = "RUB";

SimpleCardReaderPresenter presenter = new SimpleCardReaderPresenter(this, statusView);
cardReaderManager = CardReaderFactory.findManager(this, cardReader, presenter, amount, currency, null);
```
