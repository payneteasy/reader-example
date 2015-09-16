package com.payneteasy.webstart.server.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class StaticServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(StaticServlet.class);

    private static final String PREFIX = "/web/";
    private final String contentType;

    public StaticServlet(String aContentType) {
        contentType = aContentType;
    }

    public StaticServlet() {
        this("text/html; charset=utf-8");
    }

    @Override
    protected void doGet(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
        String url = aRequest.getRequestURI();

        if("".equals(url) || "/".equals(url)) {
            url = "index.html";
        }

        String resource = PREFIX + url;

        InputStream in = findFileOrResource(resource);
        if(in == null) {
            LOG.error("Resource {} not found for url {} [ requestURL = {} ]", new Object[] {resource, url, aRequest.getRequestURL()});
            aResponse.sendError(HttpServletResponse.SC_NOT_FOUND, resource + " not found");
            return;
        }

        LOG.debug("{} -> {} -> {}", new Object[] {aRequest.getRequestURI(), url, resource});

        aResponse.setContentType(contentType);
        copyStream(in, aResponse.getOutputStream());
    }

    private InputStream findFileOrResource(String resource) throws FileNotFoundException {
        File file = new File(new File("server/src/main/resources"), resource);
        LOG.debug("File is {}", file.getAbsolutePath());
        if(file.exists()) {
            return new FileInputStream(file);
        } else {
            LOG.warn("File does not exists, fallback to resources");
            return getClass().getResourceAsStream(resource);
        }
    }

    private static void copyStream(InputStream in, ServletOutputStream out) throws IOException {
        try {
            byte[] buf = new byte[0x100];
            try {
                int read;
                while ( (read = in.read(buf, 0, buf.length)) > 0) {
                    out.write(buf, 0, read);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
