package ru.otus.java.basic.http.server.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.http.server.HttpRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultInternalServerErrorRequestProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(DefaultInternalServerErrorRequestProcessor.class.getName());
    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        logger.error("500 Internal Server Error");
        String response = "HTTP/1.1 500 Internal Server Error\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>INTERNAL SERVER ERROR</h1></body></html>";
        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
