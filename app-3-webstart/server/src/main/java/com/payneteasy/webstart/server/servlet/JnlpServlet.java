package com.payneteasy.webstart.server.servlet;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.payneteasy.webstart.server.WebServerParameters;
import com.payneteasy.webstart.server.model.InvoiceJnlpModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JnlpServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(JnlpServlet.class);

    private final Mustache template;
    private final WebServerParameters parameters;

    public JnlpServlet(WebServerParameters aParameters) {
        parameters = aParameters;
        MustacheFactory mf = new DefaultMustacheFactory();
        template = mf.compile("invoice.jnlp");
    }

    @Override
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        aResponse.setContentType("application/x-java-jnlp-file");

        String jnlpFile = aRequest.getRequestURI().replace("/", "");
        String invoice = parseInvoice(aRequest.getRequestURI());

        InvoiceJnlpModel model = new InvoiceJnlpModel();
        model.codebase_url = parameters.url;
        model.jnlp_file = jnlpFile;
        model.invoice = invoice;

        template.execute(new PrintWriter(System.out), model);

        template.execute(aResponse.getWriter(), model);

    }

    private String parseInvoice(String aUri) {
        return aUri.replace("/invoice-", "").replace(".jnlp", "");
    }
}
