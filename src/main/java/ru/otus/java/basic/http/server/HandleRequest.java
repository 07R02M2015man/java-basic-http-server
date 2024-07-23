package ru.otus.java.basic.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HandleRequest {
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
            httpRequest.printInfo(false);

            dispatcher.execute(httpRequest, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private void disconnect() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
