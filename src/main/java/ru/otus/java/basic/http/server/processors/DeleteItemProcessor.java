package ru.otus.java.basic.http.server.processors;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.http.server.BadRequestException;
import ru.otus.java.basic.http.server.HttpRequest;
import ru.otus.java.basic.http.server.app.Item;
import ru.otus.java.basic.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteItemProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(DeleteItemProcessor.class.getName());
    private final ItemsRepository itemsRepository;

    public DeleteItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        if ((request.getCountItem() != 1) || !request.containsParameter("id")) {
            logger.error("Ошибка при задании параметра для удаления");
            throw new BadRequestException("Ошибка при задании параметра для удаления");
        }
        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
            itemsRepository.delete(id);
            logger.info("Элемент с id = {} успешно удален", id);
        } catch (NumberFormatException e) {
            logger.error("Некорректный формат входящего JSON объекта", e);
            throw new BadRequestException("Некорректный формат входящего JSON объекта");
        }
        String response = "HTTP/1.1 200 ОК\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n";
        out.write(response.getBytes(StandardCharsets.UTF_8));

    }
}
