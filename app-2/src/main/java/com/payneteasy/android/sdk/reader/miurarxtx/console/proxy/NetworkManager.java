package com.payneteasy.android.sdk.reader.miurarxtx.console.proxy;

import com.payneteasy.android.sdk.logger.ILogger;
import com.payneteasy.android.sdk.util.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Collections.list;

public class NetworkManager  {

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final ILogger LOG = LoggerUtil.create(NetworkManager.class);

    private final int port;

    public NetworkManager(int aPort)  {
        port = aPort;
    }

    public void open() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(findAddress(), port));
    }

    public void acceptConnection() throws IOException {
        LOG.debug("Listening on {}:{} ...", serverSocket.getInetAddress().toString(), serverSocket.getLocalPort());
        socket = serverSocket.accept();
        LOG.debug("Accepted: %s", socket.toString());
        socket.setSoTimeout(60000);

    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public static InetAddress findAddress() throws SocketException {
        List<NetworkInterface> interfaces = list(NetworkInterface.getNetworkInterfaces());

        for (NetworkInterface iface : interfaces) {
            List<InetAddress> addresses = list(iface.getInetAddresses());
            for (InetAddress address : addresses) {
                if(address.isLoopbackAddress()) {
                    continue;
                }
                if(isIPv4Address(address.getHostAddress().toUpperCase())) {
                    return address;
                }
            }
        }

        throw new IllegalStateException("No internet addresses in " + interfaces);
    }

    private static boolean isIPv4Address(String aAddress) {
        return IPV4_PATTERN.matcher(aAddress).matches();
    }

    private Socket socket;
    private ServerSocket serverSocket;

    public void close() {
        LOG.debug("Closing connection");
        try {
            socket.getOutputStream().flush();
            socket.getOutputStream().close();
//            socket.getInputStream().close();
//            socket.shutdownOutput();
//            socket.shutdownInput();
            socket.close();
        } catch (IOException e) {
            LOG.error("error closing socket", e);
        }
    }

    public void stopListening() {
        if(serverSocket==null) {
            LOG.warn("Server socket in not bound");
        } else {
            try {
                serverSocket.close();
            } catch (Exception e) {
                LOG.error("Error closing server socket", e);
            }
        }
    }
}
