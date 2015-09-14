package com.payneteasy.webstart.client;


import com.payneteasy.webstart.common.PaymentInfo;

import java.awt.*;
import java.math.BigDecimal;
import java.util.UUID;

public class ReaderApplet extends java.applet.Applet {

    MainPanel mainPanel;

    @Override
    public void init() {
        PaymentInfo payment = new PaymentInfo();
        payment.amount = BigDecimal.ONE;
        payment.currency = "RUB";
        payment.description = "desc";
        payment.invoice = UUID.randomUUID()+"";

        mainPanel = new MainPanel(payment);
        setLayout(new CardLayout());
        add(mainPanel);
    }

    @Override
    public void start() {
        mainPanel.showReaderStatus("Hello");
    }
}
