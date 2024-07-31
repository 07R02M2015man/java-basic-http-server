package ru.otus.java.basic.http.server.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.http.server.BadRequestException;
import ru.otus.java.basic.http.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(CalculatorRequestProcessor.class.getName());
    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        if (!request.containsParameter("a")) {
            logger.error("Parameter 'a' is missing");
            throw new BadRequestException("Parameter 'a' is missing");
        }
        if (!request.containsParameter("b")) {
            logger.error("Parameter 'b' is missing");
            throw new BadRequestException("Parameter 'b' is missing");
        }
        int a, b;
        try {
            a = Integer.parseInt(request.getParameter("a"));
        } catch (NumberFormatException e) {
            logger.error("Parameter 'a' has incorrect type");
            throw new BadRequestException("Parameter 'a' has incorrect type");
        }
        try {
            b = Integer.parseInt(request.getParameter("b"));
        } catch (NumberFormatException e) {
            logger.error("Parameter 'b' has incorrect type");
            throw new BadRequestException("Parameter 'b' has incorrect type");
        }

        String result = a + " + " + b + " = " + (a + b);
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>" + result + "</h1></body></html>";
        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
