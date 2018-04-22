mPOS SDK for Android and Java
=============================

[![Build Status](https://travis-ci.org/payneteasy/reader-example.svg?branch=master)](https://travis-ci.org/payneteasy/reader-example)

SDK provides fast, easy integration with mPOS readers in mobile apps.

Features
--------
* Runs both on Android and Java
* MSR, EMV Contact, EMV Contactless transactions
* Signature or PIN based transactions
* Remote key injection
* Remote firmware update
* Remote mPOS configuration
* Certified with OpenWay. The first mobile POS Chip&PIN solution in Russia - http://www.openwaygroup.com/news_card.jsp?dcion=908&rgn=1&lng=1
* Custom LCD messages for all stages (Miura only)
* Unified API for all readers
* Implemented PaynetEasy Server API https://payneteasy.com/support/documents/introduction-mpos-integration.html
* Many transaction types: sale, preauthorization, capture, cancel, reversal, partial reversal and etc.

Supported Readers
-----------------
### Production
* Miura Shutle, Miura M007, Miura M010. Supports MSR, Contact, Contactless transactions. With or without PIN. Remote keys injection, remote configuration and remote MPI and OS update
* Spire Spm2 (comming soon): supports all spm2 features
* Verifone Vx820 (usb, rs232, ethernet) 
* PAX SP30 (usb, rs232, ethernet)

### Deprecated from the 1st of January 2015 
* GD Seed: Integrated with the official SDK 
* GD Seed: Own SDK. With memory and performance optimization.
* ID Tech Unimag II: Integrated with the official SDK 
* ID Tech Unimag II: Own SDK. Memory and performance optimization. Extends supported phone models.
* Bluebamboo P25

### Evaluated
* Datecs DRD50, DRD10
* Datecs MPED400, Bluepad 50
* Wizar POS
* BBPOS

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
*   Or Oracle JVM (tested on 1.8)

### Setup maven

Add the repository to your pom.xml
```xml
<repository>
    id>reader-repo</id>
    <name>reader repo</name>
    <url>http://paynet-qa.clubber.me/reader/maven</url>
</repository>
```

### Setup gradle

Add to your repositories section
```
repositories {
...
    maven { url "https://jitpack.io" }
    maven { url "http://paynet-qa.clubber.me/reader/maven" }
...
}
```

Add to your dependencies 
```
dependencies {
...
    def readerVersion = // check the latest version at https://github.com/payneteasy/reader-example/wiki/Changelog
    compile 'com.payneteasy.android.reader:api:' + readerVersion
    compile ('com.payneteasy.android.reader:lib:' + readerVersion)

    compile 'com.payneteasy.android.reader.readers:readers-common-bluetooth:' + readerVersion
    compile ('com.payneteasy.android.reader.readers:readers-miura:' + readerVersion) {
        exclude group:'junit', module:'junit'
    }
... 
}
```
You can find the full gradle example at https://github.com/payneteasy/reader-example-gradle

### For Bluetooth Readers (Android)

Add to your AndroidManifest.xml
```xml
    <uses-permission android:name="android.permission.BLUETOOTH"/>
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
```

### For USB terminals (Android)

Add new file res/xml/device_filter.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <!-- 0x1234 / 0x0101  PAX / S80 -->
    <usb-device vendor-id="4660" product-id="257" class="255" subclass="0" protocol="0"/>

    <!-- 0x11CA / 0x0219  VeriFone Inc / Trident USB Device 1.1 / bInterfaceClass = 10 CDC Data -->
    <usb-device vendor-id="4554" product-id="537" class="10" subclass="0" protocol="0"/>

</resources>
```

Add to your AndroidManifest.xml to any activity element:
```xml
    <intent-filter>
        <action android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" />
    </intent-filter>
    <meta-data  android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" android:resource="@xml/device_filter" />
```

Add to the activity:
```java
public class MainActivity extends Activity {

    private final UsbPermissionResolver usbPermissionResolver = new UsbPermissionResolver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        usbPermissionResolver.checkPermission(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        usbPermissionResolver.checkPermission(intent, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usbPermissionResolver.unregister(this);
    }
    
    ...
    
}
```

Please see this commit for more information https://github.com/payneteasy/reader-example-gradle/commit/2aa02c47f38cef4f3a673e98fc5d5ecb98720656

### For Miura (Java)

Please see the example app-2 how to run and assemble the application for Miura Readers to run on JVM.

app-3-webstart shows how to run the application from a browser via Java WebStart:
* Linux https://www.youtube.com/watch?v=rLSx3SrkMqA
* Mac OS X - https://www.youtube.com/watch?v=OgdUfHqBvmI
* Windows - https://www.youtube.com/watch?v=I6PPO-j9KSk


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

Starts the Reader Manager

```java
CardReaderInfo cardReader = CardReaderInfo.TEST;
BigDecimal amount = new BigDecimal(1);
String currency = "RUB";

SimpleCardReaderPresenter presenter = new SimpleCardReaderPresenter(this, statusView);
cardReaderManager = CardReaderFactory.findManager(this, cardReader, presenter, amount, currency, null);
```

Deal with cardReaderManager:

* ICardReaderManager.onActivityCreate() creates resources for Bluetooth, Network
* ICardReaderManager.onActivityResume() - starts connection to a Bluetooth terminal.
* ICardReaderManager.onActivityPause() - disconnects from a Bluetooth terminal
* ICardReaderManager.onActivityDestroy() - releases resources
