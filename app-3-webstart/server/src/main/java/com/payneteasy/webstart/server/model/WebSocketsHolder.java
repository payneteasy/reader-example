package com.payneteasy.webstart.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class WebSocketsHolder {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketsHolder.class);

    final ConcurrentHashMap<String, IWebSocketClient> map = new ConcurrentHashMap<>();

    public void registerClient(String aInvoice, IWebSocketClient aClient) {
        map.put(aInvoice, aClient);
    }

    public void removeClient(String aInvoice) {
        map.remove(aInvoice);
    }

    public void sendStatus(String invoice, String aStatus) {
        IWebSocketClient client = map.get(invoice);
        if(client!=null) {
            client.sendMessage(aStatus);
        } else {
            LOG.warn("There are not client for invoice {}", invoice);
        }
    }
}
