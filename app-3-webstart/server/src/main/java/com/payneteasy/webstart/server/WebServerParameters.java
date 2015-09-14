package com.payneteasy.webstart.server;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "webserver", description = "payment web server")
public class WebServerParameters {

    @Option(name = {"-p", "--port"}, description = "Web server port. default is 8080")
    public int port = 8080;

    @Option(name = {"-u", "--base-url"}, description = "Public base URL, default is http://localhost:8080/")
    public String url = "http://localhost:8080/";
}
