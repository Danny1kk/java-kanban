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
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET" -> {
                    if (query == null) {
                        List<Subtask> subtasks = manager.getAllSubtasks();
                        sendText(exchange, gson.toJson(subtasks), 200);
                    } else {
                        int id = Integer.parseInt(query.split("=")[1]);
                        Subtask subtask = manager.getSubtask(id);
                        if (subtask != null) {
                            sendText(exchange, gson.toJson(subtask), 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                }
                case "POST" -> {
                    InputStream is = exchange.getRequestBody();
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = gson.fromJson(body, Subtask.class);

                    if (subtask.getId() == 0) {
                        manager.addSubtask(subtask);
                        sendText(exchange, "Subtask created", 201);
                    } else {
                        manager.updateSubtask(subtask);
                        sendText(exchange, "Subtask updated", 201);
                    }
                }
                case "DELETE" -> {
                    if (query == null) {
                        manager.deleteAllSubtasks();
                        sendText(exchange, "All subtasks deleted", 200);
                    } else {
                        int id = Integer.parseInt(query.split("=")[1]);
                        manager.removeSubtask(id);
                        sendText(exchange, "Subtask deleted", 200);
                    }
                }
                case null, default -> sendText(exchange, "Unsupported method", 405);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}