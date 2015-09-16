package com.payneteasy.webstart.client;

import com.payneteasy.webstart.common.PaymentInfo;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.awt.GridBagConstraints.*;
import static javax.swing.SwingUtilities.invokeLater;

public class MainPanel extends JPanel implements IPaymentStatus {

    private static final Logger LOG = LoggerFactory.getLogger(MainPanel.class);

    final Executor executor = Executors.newSingleThreadExecutor();

    final PaymentInfo paymentInfo;

    final JTextArea readerStatus  = new JTextArea("Connecting ...");
    final JTextArea paymentStatus = new JTextArea("Initializing ...");

    public MainPanel(PaymentInfo aPaymentInfo) {
        paymentInfo = aPaymentInfo;

        setLayout(new GridBagLayout());

        readerStatus.setWrapStyleWord(true);
        readerStatus.setLineWrap(true);

        paymentStatus.setWrapStyleWord(true);
        paymentStatus.setLineWrap(true);

        // the first row
        Insets insets = new Insets(10, 10, 5, 10);

        add(new JLabel("Reader Status: "), new GridBagConstraints(0, 0, 1, 1, 0, 0, NORTHWEST, NONE, insets, 0, 0));
        add(readerStatus, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, CENTER, BOTH, insets, 0, 0));

        // the second row
        add(new JLabel("Payment Status: "), new GridBagConstraints(0, 2, 1, 1, 0, 0, NORTHWEST, BOTH, insets, 0, 0));
        add(paymentStatus, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, CENTER, BOTH, insets, 0, 0));

    }

    @Override
    public void showReaderStatus(String aStatus) {
        LOG.debug("Reader: {}", aStatus);
        invokeLater(() -> readerStatus.setText(trimText(aStatus)));
        executor.execute(() -> sendMessage(aStatus));
    }

    @Override
    public void showPaymentStatus(String aStatus) {
        LOG.debug("Payment: {}", aStatus);
        invokeLater(() -> paymentStatus.setText(trimText(aStatus)));
        executor.execute(() -> sendMessage(aStatus));
    }

    private final OkHttpClient client = new OkHttpClient();

    private void sendMessage(String aStatus) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("invoice", paymentInfo.invoice)
                .add("message", aStatus)
                .build();

        Request request = new Request.Builder()
                .url(paymentInfo.updateStatusUrl)
                .post(formBody)
                .build();

        try {
            LOG.debug("Sending for invoice {} message '{}'", paymentInfo.invoice, aStatus);
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            LOG.debug(response.body().string());
        } catch (Exception e) {
            LOG.error("Could not send post", e);
        }
    }

    private String trimText(String aStatus) {
        return aStatus != null && aStatus.length() > 500
                ? aStatus.substring(0, 500) + "..."
                : aStatus;
    }

    public static void main(String[] args) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.invoice = "123";

        new MainPanel(paymentInfo).sendMessage("Hello 123");
    }
}
