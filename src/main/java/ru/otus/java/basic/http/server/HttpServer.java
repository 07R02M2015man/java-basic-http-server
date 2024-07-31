package ru.otus.java.basic.http.server;

import ru.otus.java.basic.http.server.processors.AnotherHelloWorldRequestProcessor;
import ru.otus.java.basic.http.server.processors.HelloWorldRequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class.getName());
    private int port;
    private Dispatcher dispatcher;
    private final ExecutorService executorService;


    public HttpServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту: {}", port);
//                try (Socket socket = serverSocket.accept()) {
//                byte[] buffer = new byte[8192];
//                int n = socket.getInputStream().read(buffer);
//                String rawRequest = new String(buffer, 0, n);
//                HttpRequest request = new HttpRequest(rawRequest);
//                request.printInfo(true);
//                dispatcher.execute(request, socket.getOutputStream());
//            }
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> {
                    try {
                        new HandleRequest(socket);
                    } catch (IOException e) {
                        logger.error(e);
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            logger.error(e);
        } finally {
            executorService.shutdown();
        }
    }

}