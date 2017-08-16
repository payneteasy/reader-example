package com.payneteasy.android.sdk.reader.miurarxtx.console.proxy;

public class ProxyServer {

    public static void main(String[] args) throws Exception {

        SerialProxyServerTask task = new SerialProxyServerTask(3128, args[0]);
        task.start();

        System.in.read();

        task.interrupt();

        Thread.sleep(1000);

        System.exit(0);
    }
}
