package tasks;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET" -> {
                    if (query == null) {
                        List<Task> tasks = manager.getAllTasks();
                        sendText(exchange, gson.toJson(tasks), 200);
                    } else {
                        int id = Integer.parseInt(query.split("=")[1]);
                        Task task = manager.getTask(id);
                        if (task != null) {
                            sendText(exchange, gson.toJson(task), 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                }
                case "POST" -> {
                    InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(reader, Task.class);

                    if (task.getId() == 0) {
                        manager.addTask(task);
                        sendText(exchange, "Задача создана", 201);
                    } else {
                        manager.updateTask(task);
                        sendText(exchange, "Задача обновлена", 201);
                    }
                }
                case "DELETE" -> {
                    if (query == null) {
                        manager.deleteAllTasks();
                        sendText(exchange, "Все задачи удалены", 200);
                    } else {
                        int id = Integer.parseInt(query.split("=")[1]);
                        manager.removeTask(id);
                        sendText(exchange, "Задача удалена", 200);
                    }
                }
                case null, default -> sendText(exchange, "Неподдерживаемый метод", 405);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}