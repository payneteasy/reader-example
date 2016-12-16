package com.payneteasy.webstart.client;

import com.payneteasy.android.sdk.processing.impl.EmvProcessingService;
import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderType;
import com.payneteasy.android.sdk.reader.ReaderConfigContext;
import com.payneteasy.android.sdk.reader.miurarxtx.MiuraRxTxReader;
import com.payneteasy.android.sdk.reader.rxtx.github.RxTxSerialManagerGithub;
import com.payneteasy.webstart.common.PaymentInfo;
import io.airlift.airline.SingleCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.LogManager;

import static javax.swing.JOptionPane.showMessageDialog;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Version 4");

        try {
            LogManager logManager = LogManager.getLogManager();
            logManager.readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame.setDefaultLookAndFeelDecorated(true);

        PaymentInfo payment;
        try {
            payment = parseParameters(args);
        } catch (Exception e) {
            LOG.error("Could not parse parameters", e);
            showMessageDialog(null, e.getMessage());
            System.exit(0);
            return;
        }

        JFrame frame = new JFrame("Payment");
        MainPanel mainPanel = new MainPanel(payment);

        showMainFrame(payment, frame, mainPanel);
        startPayment(frame, mainPanel, payment);
    }

    private static void showMainFrame(PaymentInfo payment, JFrame frame, MainPanel mainPanel) {
        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(() -> {
            frame.setTitle(payment.invoice);
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            frame.setAlwaysOnTop(true);
//            frame.setUndecorated(true);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width / 3, 500);
            frame.setLocationRelativeTo(null);

            // Displays the window
            frame.setVisible(true);
        });
    }

    private static PaymentInfo parseParameters(String[] args) {
        StartParameters parameters = SingleCommand
                .singleCommand(StartParameters.class)
                .parse(args);

        PaymentInfo payment = new PaymentInfo();
        payment.amount      = parameters.amount;
        payment.currency    = parameters.currency;
        payment.invoice     = parameters.invoice;
        payment.description = parameters.description;
        payment.updateStatusUrl = parameters.updateStatusUrl;

        return payment;
    }

    private static void startPayment(JFrame aFrame, MainPanel aMainPanel, PaymentInfo aPayment) {
        // "/dev/ttyACM0"
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
            manager.start(new RxTxSerialManagerGithub());

            aFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    aFrame.setTitle("Exiting ...");
                    LOG.debug("Stop manager ...");
                    try {
                        manager.stop();
                    } catch (Exception e1) {
                        LOG.error("Could not stop the reader manager", e);
                    }
                }
            });
        } catch (Exception e) {
            aMainPanel.showReaderStatus(e.getMessage());

            LOG.error("Error", e);
        }

    }


}
