package com.payneteasy.webstart.server;

import com.payneteasy.webstart.server.model.WebSocketsHolder;
import com.payneteasy.webstart.server.servlet.*;
import io.airlift.airline.SingleCommand;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

    public static void main(String[] args) throws Exception {
        WebServerParameters parameters = parseParameters(args);
        Server server = new WebServer().start(parameters);
        LOG.info("Base url is {}", parameters.url);
        server.join();
    }

    private static WebServerParameters parseParameters(String[] args) {
        return SingleCommand
                .singleCommand(WebServerParameters.class)
                .parse(args);
    }

    public Server start(WebServerParameters aParameters) throws Exception {
        Server  server = new Server(aParameters.port);

        WebSocketsHolder webSocketsHolder = new WebSocketsHolder();

        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(StaticServlet.class, "/");
        context.addServlet(StaticServlet.class, "*.html");
        context.addServlet(new ServletHolder(new StaticServlet("application/java-archive")), "*.jar");
        context.addServlet(CheckoutServlet.class, "/checkout.do");
        context.addServlet(new ServletHolder(new JnlpServlet(aParameters)), "*.jnlp");

        context.addServlet(new ServletHolder(new UpdateStatusServlet(webSocketsHolder)), "/update-status.do");
        context.addServlet(new ServletHolder(new PaymentStatusWebsocketServlet(webSocketsHolder)), "/status.ws");

        server.setHandler(context);

        server.start();
        return server;
    }

}
