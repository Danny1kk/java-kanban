package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private static HttpTaskServer server;
    private final Gson gson = HttpTaskServer.getGson();
    private static HttpClient client;

    @BeforeAll
    static void beforeAll() throws IOException {
        TaskManager manager = new InMemoryTaskManager();

        server = new HttpTaskServer(manager);
        server.start();

        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(
                "Test task",
                "Test description",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30)
        );

        String json;
        json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "При добавлении задачи должен вернуться код 201");
    }
}