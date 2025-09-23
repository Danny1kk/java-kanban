package tasks;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            if ("POST".equalsIgnoreCase(method)) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(body, Subtask.class);

                if (subtask.getId() == 0) {
                    manager.addSubtask(subtask);
                    sendText(exchange, gson.toJson(subtask), 201);
                } else {
                    manager.updateSubtask(subtask);
                    sendText(exchange, gson.toJson(subtask), 200);
                }

            } else if ("GET".equalsIgnoreCase(method)) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    Subtask subtask = manager.getSubtask(id);
                    if (subtask != null) {
                        sendText(exchange, gson.toJson(subtask), 200);
                    } else {
                        sendText(exchange, "Подзадача не найден", 404);
                    }
                } else {
                    sendText(exchange, gson.toJson(manager.getAllSubtasks()), 200);
                }

            } else {
                sendText(exchange, "Метод не поддерживается", 405);
            }

        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}