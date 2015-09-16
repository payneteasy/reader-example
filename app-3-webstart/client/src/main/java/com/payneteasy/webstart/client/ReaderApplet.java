package com.payneteasy.webstart.client;


import com.payneteasy.android.sdk.processing.impl.EmvProcessingService;
import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderType;
import com.payneteasy.android.sdk.reader.ReaderConfigContext;
import com.payneteasy.android.sdk.reader.miurarxtx.MiuraRxTxReader;
import com.payneteasy.webstart.common.PaymentInfo;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ReaderApplet extends java.applet.Applet {

    private static final Logger LOG = LoggerFactory.getLogger(ReaderApplet.class);

    MainPanel mainPanel;
    MiuraRxTxReader manager;
    PaymentInfo payment;

    @Override
    public void init() {
        payment = new PaymentInfo();
        payment.amount      = new BigDecimal(getParameter("amount"));
        payment.currency    = getParameter("currency");
        payment.description = getParameter("desc");
        payment.invoice     = getParameter("invoice");

        mainPanel = new MainPanel(payment);
        setLayout(new CardLayout());
        add(mainPanel);
    }

    @Override
    public void start() {
        mainPanel.showReaderStatus("Hello");
        startPayment(mainPanel, payment);
//        new Thread(() -> ).start();
    }

    private static void startPayment(MainPanel aMainPanel, PaymentInfo aPayment) {
        CardReaderInfo readerInfo = new CardReaderInfo("Miura", CardReaderType.MIURA, null);
        EmvProcessingService processingService = new EmvProcessingService(null, readerInfo, aPayment.amount, aPayment.currency);
        ReaderConfigContext context = new ReaderConfigContext.Builder()
                .amount            ( aPayment.amount                             )
                .currency          ( aPayment.currency                           )
                .readerInfo        ( readerInfo                                  )
                .presenter         ( new CardReaderListener(aMainPanel, aPayment))
                .processingService ( processingService                           )
                .build();

        MiuraRxTxReader manager = new MiuraRxTxReader(context);
        try {
            LOG.debug("Starting reader manager ...");
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        manager.start();
                    } catch (Exception e) {
                        LOG.error("Priv action", e);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            aMainPanel.showReaderStatus(e.getMessage());
            LOG.error("Error", e);
        }

    }

    @Override
    public void stop() {
        if(manager != null) {
            try {
                manager.stop();
            } catch (Exception e) {
                LOG.error("Could not stop manager", e);
            }
        }
    }
}
