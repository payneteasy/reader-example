package com.payneteasy.webstart.server.websocket;

import com.payneteasy.webstart.server.model.IWebSocketClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebSocket
public class PaymentStatusWebsocket implements IWebSocketClient{

    private static final Logger LOG = LoggerFactory.getLogger(PaymentStatusWebsocket.class);

    private Session session;
    private final String invoice;

    public PaymentStatusWebsocket(String aInvoice) {
        invoice = aInvoice;
    }

    @OnWebSocketConnect
    public void handleConnect(Session session) {
        this.session = session;
        LOG.debug("Connected with invoice "+invoice);
        sendMessage("Invoice is "+invoice);
    }

    public void sendMessage(String aMessage) {
        try {
            if(session.isOpen()) {
                session.getRemote().sendString(aMessage);
                LOG.debug("Message sent: {}", aMessage);
            } else {
                LOG.warn("Session for {} is not open", invoice);
            }
        } catch (IOException e) {
            LOG.error("Could not send a message", e);
        }

    }


    @OnWebSocketClose
    public void handleClose(int statusCode, String reason) {
        LOG.debug("Connection closed with statusCode=" + statusCode + ", reason=" + reason);
    }


    @OnWebSocketMessage
    public void handleMessage(String message) {
        LOG.debug("message is {}", message);
    }
}
