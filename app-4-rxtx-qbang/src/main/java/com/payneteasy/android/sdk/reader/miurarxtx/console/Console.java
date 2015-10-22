package com.payneteasy.android.sdk.reader.miurarxtx.console;

import com.payneteasy.android.sdk.processing.impl.EmvProcessingService;
import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderType;
import com.payneteasy.android.sdk.reader.ReaderConfigContext;
import com.payneteasy.android.sdk.reader.miurarxtx.MiuraRxTxDetector;
import com.payneteasy.android.sdk.reader.miurarxtx.MiuraRxTxReader;
import com.payneteasy.android.sdk.reader.rxtx.api.ISerialPort;
import com.payneteasy.android.sdk.reader.rxtx.api.SerialPortInUseException;
import com.payneteasy.android.sdk.reader.rxtx.api.UnsupportedSerialOperationException;
import com.payneteasy.android.sdk.reader.rxtx.qbang.RxTxSerialManagerQbang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

import static com.payneteasy.android.sdk.reader.miurarxtx.MiuraRxTxDetector.isMiuraConnected;

public class Console {

    private final static Logger LOG = LoggerFactory.getLogger(Console.class);

    public static void main(String[] args)  {
        CardReaderInfo readerInfo = new CardReaderInfo("Miura", CardReaderType.MIURA, null);
        BigDecimal amount = BigDecimal.ONE;
        String currency = "RUB";
        EmvProcessingService processingService = new EmvProcessingService(null, readerInfo, amount, currency);
        ReaderConfigContext context = new ReaderConfigContext.Builder()
                .amount            ( amount                    )
                .currency          ( currency                  )
                .readerInfo        ( readerInfo                )
                .presenter         ( new ConsolePresenter()  )
                .processingService ( processingService         )
                .build();

        MiuraRxTxReader reader = new MiuraRxTxReader(context);

        try {
            reader.start( findMiuraPort() );

            System.out.printf("To stop, press the ENTER key");
            System.in.read();

            reader.stop();
        } catch (Exception e) {
            LOG.error("Error", e);
        }

    }

    private static ISerialPort findMiuraPort() throws SerialPortInUseException, UnsupportedSerialOperationException, IOException {
        RxTxSerialManagerQbang manager = new RxTxSerialManagerQbang();
        for (ISerialPort port : manager.listPorts()) {
            if(isMiuraConnected(port)) {
                return port;
            }
        }
        throw new IllegalStateException("Miura not connected");
    }
}
