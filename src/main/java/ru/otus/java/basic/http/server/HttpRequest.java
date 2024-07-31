package ru.otus.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LogManager.getLogger(HttpRequest.class.getName());
    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private String body;
    private Map<String, String> parameters;
    private Map<String, String> headers;


    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getBody() {
        return body;
    }

    public String getUri() {
        return uri;
    }

    public int getCountItem() {
        return parameters.size();
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        this.headers = new HashMap<>();

        //парсинг заголовков
        int startIndexHeader = rawRequest.indexOf("\r\n") + 2;
        int endIndexHeader = rawRequest.indexOf("\r\n\r\n");
        String allHeaders = rawRequest.substring(startIndexHeader, endIndexHeader);
        String[] differentHeaders = allHeaders.split("\r\n");
        for (String header : differentHeaders) {
            String[] keyValue = header.split(":");
            String headerKey = keyValue[0];
            String headerValue = keyValue[1];
            headers.put(headerKey, headerValue);
        }

        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(rawRequest.indexOf("\r\n\r\n") + 4);
        }
    }

    public void printInfo(boolean showRawRequest) {
        logger.info("uri: {}", uri);
        logger.info("method: {}", method);
        logger.info("body: {}", body);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            logger.info("headers: {}: {}", entry.getKey(), entry.getValue());
        }
    }

}
