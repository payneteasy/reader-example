package com.payneteasy.webstart.client;

import com.payneteasy.android.sdk.card.BankCard;
import com.payneteasy.android.sdk.processing.ConfigurationContinuation;
import com.payneteasy.android.sdk.processing.ProcessingContinuation;
import com.payneteasy.android.sdk.reader.CardError;
import com.payneteasy.android.sdk.reader.CardReaderEvent;
import com.payneteasy.android.sdk.reader.CardReaderProblem;
import com.payneteasy.android.sdk.reader.ICardReaderPresenter;
import com.payneteasy.webstart.common.PaymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardReaderListener implements ICardReaderPresenter {

    private static final Logger LOG = LoggerFactory.getLogger(CardReaderListener.class);

    private final PaymentInfo payment;
    private final IPaymentStatus status;

    public CardReaderListener(IPaymentStatus aStatus, PaymentInfo aPayment) {
        status = aStatus;
        payment = aPayment;
    }

    @Override
    public void cardReaderStateChanged(CardReaderEvent aEvent) {
        status.showReaderStatus(aEvent.getState()+" - "+aEvent.getMessage());
    }

    @Override
    public ProcessingContinuation onCard(BankCard aCard) {
        LOG.debug("onCard {}", aCard);
        status.showReaderStatus("Got a card: " + aCard);
        return ProcessingContinuation.Builder
                .startSaleOnline()
                .processingBaseUrl("https://paynet-qa.clubber.me/paynet")
                .merchantLogin("paynet-demo")
                .merchantControlKey("5D0BB936-46BB-11E5-A54A-735A47388A3F")
                .merchantEndPointId(1)
                .orderDescription(payment.description)
                .orderInvoiceNumber(payment.invoice)
                .orderMerchantData("custom merchant data for internal use")
                .customerPhone("+7 (499) 918-64-41")
                .customerEmail("info@payneteasy.com")
                .customerCountry("RUS")
                .listener(aEvent -> {
                    LOG.debug("Stage event: {}", aEvent);
                    status.showPaymentStatus(aEvent.type+ " - "+aEvent.toString());
                })
                .build();
    }

    @Override
    public void onCardError(CardError aError) {
        status.showReaderStatus("Card Error: " + aError);
    }

    @Override
    public void onReaderNotSupported(CardReaderProblem aProblem) {
        status.showReaderStatus("Reader Error: " + aProblem);
    }

    @Override
    public ConfigurationContinuation onConfiguration() {
        return null;
    }

    @Override
    public void onAudioData(short[] shorts, int i) {
        // does not use if a bluetooth reader
    }

    @Override
    public void onReaderSerialNumber(String s) {
        // does not use if a bluetooth reader
    }

}
