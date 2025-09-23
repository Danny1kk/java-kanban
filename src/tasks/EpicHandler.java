package tasks;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            if ("POST".equalsIgnoreCase(method)) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(body, Epic.class);

                if (epic.getId() == 0) {
                    manager.addEpic(epic);
                    sendText(exchange, gson.toJson(epic), 201);
                } else {
                    manager.updateEpic(epic);
                    sendText(exchange, gson.toJson(epic), 200);
                }

            } else if ("GET".equalsIgnoreCase(method)) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    Epic epic = manager.getEpic(id);
                    if (epic != null) {
                        sendText(exchange, gson.toJson(epic), 200);
                    } else {
                        sendText(exchange, "Эпик не найден", 404);
                    }
                } else {
                    sendText(exchange, gson.toJson(manager.getAllEpics()), 200);
                }

            } else {
                sendText(exchange, "Метод не поддерживается", 405);
            }

        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}