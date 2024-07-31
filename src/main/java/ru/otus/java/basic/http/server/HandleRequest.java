package ru.otus.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HandleRequest {
    private static final Logger logger = LogManager.getLogger(Server.class.getName());
    private final byte[] buffer;
    private final Dispatcher dispatcher;
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;

    public HandleRequest(Socket socket) throws IOException {
        this.buffer = new byte[8192];
        this.dispatcher = new Dispatcher();
        this.socket = socket;
        this.out = socket.getOutputStream();
        this.in = socket.getInputStream();

        try {
            int n = in.read(buffer);
            String rawRequest = new String(buffer, 0, n);
            HttpRequest httpRequest = new HttpRequest(rawRequest);
            httpRequest.printInfo();
            dispatcher.execute(httpRequest, out);
        } catch (IOException e) {
            logger.error("Ошибка парсинга", e);
        } finally {
            disconnect();
        }
    }

    private void disconnect() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("Ошибка закрытия входящего потока", e);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("Ошибка закрытия исходящего потока",e);
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            logger.error("Ошибка закрытия сокета", e);
        }
    }
}
