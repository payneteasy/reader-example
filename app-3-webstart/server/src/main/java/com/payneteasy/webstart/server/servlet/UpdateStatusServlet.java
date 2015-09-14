package com.payneteasy.webstart.server.servlet;

import com.payneteasy.webstart.server.model.WebSocketsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateStatusServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateStatusServlet.class);

    private final WebSocketsHolder holder;

    public UpdateStatusServlet(WebSocketsHolder aHolder) {
        holder = aHolder;
    }

    @Override
    protected void service(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        String invoice = aRequest.getParameter("invoice");
        String message = aRequest.getParameter("message");

        LOG.debug("Got invoice {} and message {}", invoice, message);
        holder.sendStatus(invoice, message);
    }
}
