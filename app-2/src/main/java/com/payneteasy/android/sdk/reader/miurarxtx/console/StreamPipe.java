package com.payneteasy.android.sdk.reader.miurarxtx.console;

import com.payneteasy.android.sdk.logger.ILogger;
import com.payneteasy.android.sdk.util.LoggerUtil;
import com.payneteasy.tlv.HexUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

public class StreamPipe extends Thread {

    private final ILogger LOG = LoggerUtil.create(StreamPipe.class);

    public StreamPipe(String aName, CountDownLatch aLatch, InputStream aInput, OutputStream aOut) {
        input = aInput;
        output = aOut;
        name = aName;
        latch = aLatch;
        setName( name );
    }

    @Override
    public void run() {
        try {
            while(true) {
                LOG.debug("    {}: Waiting for data...", name);
                int count = input.read(buffer);
                if(count<0) {
                    LOG.debug("    {}: socket closed", name);
                    break;
                } else {
                    LOG.debug("    {}: {} ...", name, HexUtil.toHexString(buffer, 0, count));
                    output.write(buffer, 0, count);
                }
            }
        } catch (Exception e) {
            LOG.error("io error", e);
        }
        latch.countDown();
    }

    private final byte[] buffer = new byte[1024];
    private final InputStream input;
    private final OutputStream output;
    private String name;
    private final CountDownLatch latch;
}
