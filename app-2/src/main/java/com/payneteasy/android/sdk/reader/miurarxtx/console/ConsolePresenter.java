package com.payneteasy.android.sdk.reader.miurarxtx.console;

import com.payneteasy.android.sdk.card.BankCard;
import com.payneteasy.android.sdk.logger.ILogger;
import com.payneteasy.android.sdk.processing.ConfigurationContinuation;
import com.payneteasy.android.sdk.processing.IProcessingStageListener;
import com.payneteasy.android.sdk.processing.ProcessingContinuation;
import com.payneteasy.android.sdk.processing.ProcessingStageEvent;
import com.payneteasy.android.sdk.reader.CardError;
import com.payneteasy.android.sdk.reader.CardReaderEvent;
import com.payneteasy.android.sdk.reader.CardReaderProblem;
import com.payneteasy.android.sdk.reader.ICardReaderPresenter;
import com.payneteasy.android.sdk.util.LoggerUtil;

import java.io.File;

public class ConsolePresenter implements ICardReaderPresenter {

    private static final ILogger LOG = LoggerUtil.create(ConsolePresenter.class);

    @Override
    public void cardReaderStateChanged(CardReaderEvent aEvent) {
        LOG.debug("cardReaderStateChanged {}", aEvent);
    }

    @Override
    public ProcessingContinuation onCard(BankCard aBankCard) {
        LOG.debug("onCard {}", aBankCard);
        return ProcessingContinuation.Builder
                .startSaleOnline()
                .processingBaseUrl("https://paynet-qa.payneteasy.com/paynet")

                .merchantLogin("paynet-demo")
                .merchantControlKey("5D0BB936-46BB-11E5-A54A-735A47388A3F")
                .merchantEndPointId(1)
                .orderDescription("test description")
                .orderInvoiceNumber("invoice-"+System.currentTimeMillis())
                .orderMerchantData("custom merchant data for internal use")
                .customerPhone("+7 (499) 918-64-41")
                .customerEmail("info@payneteasy.com")
                .customerCountry("RUS")
                .listener(new IProcessingStageListener() {
                    @Override
                    public void onStageChanged(final ProcessingStageEvent aEvent) {
                        LOG.debug("Stage event: {}", aEvent);
                    }
                })
                .build();

    }

    @Override
    public void onCardError(CardError aError) {
        LOG.debug("onCardError {}", aError);
    }

    @Override
    public void onReaderNotSupported(CardReaderProblem aProblem) {
        LOG.debug("onReaderNotSupported {}", aProblem);

    }

    @Override
    public void onAudioData(short[] aBuffer, int aLength) {

    }

    @Override
    public void onReaderSerialNumber(String aSerialNumber) {

    }

    @Override
    public ConfigurationContinuation onConfiguration() {
        LOG.debug("onConfiguration");

        return new ConfigurationContinuation.Builder()
                .merchantLogin("paynet-demo")
                .configurationBaseUrl("https://gate.payneteasy.com/rki")
                .merchantEndPointId(1)
                .merchantControlKey("5D0BB936-46BB-11E5-A54A-735A47388A3F")
                .configDir(new File("./target/miura"))
                .build();
    }
}
