package tasks;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            if ("POST".equalsIgnoreCase(method)) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);

                if (task.getId() == 0) {
                    manager.addTask(task);
                    sendText(exchange, gson.toJson(task), 201);
                } else {
                    manager.updateTask(task);
                    sendText(exchange, gson.toJson(task), 200);
                }

            } else if ("GET".equalsIgnoreCase(method)) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    Task task = manager.getTask(id);
                    if (task != null) {
                        sendText(exchange, gson.toJson(task), 200);
                    } else {
                        sendText(exchange, "Задача не найдена", 404);
                    }
                } else {
                    sendText(exchange, gson.toJson(manager.getAllTasks()), 200);
                }

            } else {
                sendText(exchange, "Метод не поддерживается", 405);
            }

        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}