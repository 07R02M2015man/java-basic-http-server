package ru.otus.java.basic.http.server;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;
    private final ExecutorService executorService;


    public HttpServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);
//            try (Socket socket = serverSocket.accept()) {
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
                      throw new RuntimeException(e);
                  }
              });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }



}
