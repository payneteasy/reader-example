package com.payneteasy.webstart.server.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class CheckoutServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        LOG.debug("OK");

        aResponse.setContentType("application/json");
        PrintWriter out = aResponse.getWriter();
        out.println("{\"invoice\":\""+UUID.randomUUID()+"\"}");
    }
}
