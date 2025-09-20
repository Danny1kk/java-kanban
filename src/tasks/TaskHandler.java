package tasks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
                System.out.println("Получен JSON: " + body);

                try {
                    Task task = gson.fromJson(body, Task.class);
                    if (task == null) {
                        sendText(exchange, "Некорректный JSON", 400);
                        return;
                    }

                    manager.addTask(task);
                    sendText(exchange, "Задача успешно создана", 201);

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    sendText(exchange, "Ошибка парсинга JSON", 400);
                }

            } else if ("GET".equalsIgnoreCase(method)) {
                sendText(exchange, gson.toJson(manager.getAllTasks()), 200);

            } else {
                sendText(exchange, "Метод не поддерживается", 405);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendServerError(exchange, e);
        }
    }
}