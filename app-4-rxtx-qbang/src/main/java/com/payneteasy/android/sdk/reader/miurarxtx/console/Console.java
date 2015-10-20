package com.payneteasy.android.sdk.reader.miurarxtx.console;

import com.payneteasy.android.sdk.processing.impl.EmvProcessingService;
import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderType;
import com.payneteasy.android.sdk.reader.ReaderConfigContext;
import com.payneteasy.android.sdk.reader.miurarxtx.MiuraRxTxReader;
import com.payneteasy.android.sdk.reader.rxtx.qbang.RxTxSerialManagerQbang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

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

        MiuraRxTxReader manager = new MiuraRxTxReader(context);
        try {
            manager.start(new RxTxSerialManagerQbang());

            Thread.sleep(60000);

            manager.stop();
        } catch (Exception e) {
            LOG.error("Error", e);
        }

    }
}
