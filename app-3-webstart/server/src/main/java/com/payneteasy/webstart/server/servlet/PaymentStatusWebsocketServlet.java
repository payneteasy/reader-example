package com.payneteasy.webstart.server.servlet;

import com.payneteasy.webstart.server.model.WebSocketsHolder;
import com.payneteasy.webstart.server.websocket.PaymentStatusWebsocket;
import org.eclipse.jetty.websocket.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentStatusWebsocketServlet extends WebSocketServlet {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentStatusWebsocketServlet.class);

    private final WebSocketsHolder holder;

    public PaymentStatusWebsocketServlet(WebSocketsHolder aHolder) {
        holder = aHolder;
    }


    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.setCreator((aRequest, aResponse) -> {
            LOG.debug("request: {}", aRequest.getRequestURI());
            String invoice = aRequest.getParameterMap().get("invoice").get(0);
            PaymentStatusWebsocket websocket = new PaymentStatusWebsocket(invoice);
            holder.registerClient(invoice, websocket);
            return websocket;
        });
//        webSocketServletFactory.register(PaymentStatusWebsocket.class);
    }




}
