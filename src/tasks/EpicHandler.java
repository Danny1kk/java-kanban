package tasks;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET" -> {
                    if (path.endsWith("/subtasks")) {
                        int id = Integer.parseInt(query.split("=")[1]);
                        List<Subtask> subtasks = manager.getEpicSubtasks(id);
                        sendText(exchange, gson.toJson(subtasks), 200);
                    } else if (query == null) {
                        List<Epic> epics = manager.getAllEpics();
                        sendText(exchange, gson.toJson(epics), 200);
                    } else {
                        int id = Integer.parseInt(query.split("=")[1]);
                        Epic epic = manager.getEpic(id);
                        if (epic != null) {
                            sendText(exchange, gson.toJson(epic), 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                }
                case "POST" -> {
                    InputStream is = exchange.getRequestBody();
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);

                    if (epic.getId() == 0) {
                        manager.addEpic(epic);
                        sendText(exchange, "Epic created", 201);
                    } else {
                        sendText(exchange, "Epic cannot be updated", 405);
                    }
                }
                case "DELETE" -> {
                    int id = Integer.parseInt(query.split("=")[1]);
                    manager.removeEpic(id);
                    sendText(exchange, "Epic deleted", 200);
                }
                case null, default -> sendText(exchange, "Unsupported method", 405);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}