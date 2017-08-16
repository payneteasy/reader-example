package com.payneteasy.android.sdk.reader.miurarxtx.console.proxy;
import com.payneteasy.android.sdk.logger.ILogger;
import com.payneteasy.android.sdk.reader.miurarxtx.console.StreamPipe;
import com.payneteasy.android.sdk.reader.rxtx.api.ISerialPort;
import com.payneteasy.android.sdk.reader.rxtx.api.NoSuchSerialPortException;
import com.payneteasy.android.sdk.reader.rxtx.github.RxTxSerialManagerGithub;
import com.payneteasy.android.sdk.util.LoggerUtil;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
        import java.util.concurrent.atomic.AtomicInteger;

public class SerialProxyServerTask extends Thread  {

    private static final ILogger LOG = LoggerUtil.create(SerialProxyServerTask.class);

    private static final AtomicInteger THREAD_INDEX = new AtomicInteger(0);

    private final String name = "task";
    private final NetworkManager network;
    private final ISerialPort serialPort;

    public SerialProxyServerTask(int aPort, String aSerialPort) throws NoSuchSerialPortException, IOException {
        network = new NetworkManager(aPort);
        serialPort = new RxTxSerialManagerGithub().findPort(aSerialPort);
    }

    @Override
    public void run() {
        try {

            setName(name+"-"+THREAD_INDEX.incrementAndGet());

            network.open();
            while (!Thread.currentThread().isInterrupted()) {
                network.acceptConnection();

                try {
                    serialPort.open();
                    try {
                        CountDownLatch latch = new CountDownLatch(1);

                        StreamPipe networkSerial = new StreamPipe("net-bt", latch, network.getInputStream(), serialPort.getOutputStream());
                        StreamPipe serialNetwork = new StreamPipe("bt-net", latch, serialPort.getInputStream(), network.getOutputStream());

                        networkSerial.start();
                        serialNetwork.start();

                        LOG.debug("Waiting first to exist...");
                        latch.await();
                        LOG.debug("Exited");

                        networkSerial.interrupt();
                        serialNetwork.interrupt();

                    } finally {
                        serialPort.close();
                    }

                } catch (Exception e) {
                    LOG.error("Can't connect to device", e);
                } finally {
                    network.close();
                }

            }

        } catch (Exception e) {
            LOG.error("Main cycle error: "+e.getMessage(), e);
        }
    }

    public void cancel() {
        network.stopListening();
        interrupt();
    }

}
