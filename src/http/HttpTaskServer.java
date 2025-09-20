package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import manager.Managers;
import tasks.TaskHandler;
import tasks.SubtaskHandler;
import tasks.EpicHandler;
import tasks.HistoryHandler;
import tasks.PrioritizedHandler;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;
    private final TaskManager manager;
    private static final int PORT = 8080;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter out, LocalDateTime value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                        return;
                    }
                    out.value(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                }

                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    String str = in.nextString();
                    return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
            })
            .registerTypeAdapter(Duration.class, new TypeAdapter<Duration>() {
                @Override
                public void write(JsonWriter out, Duration value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                        return;
                    }
                    out.value(value.toMinutes()); // храним длительность в минутах
                }

                @Override
                public Duration read(JsonReader in) throws IOException {
                    long minutes = in.nextLong();
                    return Duration.ofMinutes(minutes);
                }
            })
            .serializeNulls()
            .create();

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager, gson));
        server.createContext("/subtasks", new SubtaskHandler(manager, gson));
        server.createContext("/epics", new EpicHandler(manager, gson));
        server.createContext("/history", new HistoryHandler(manager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(manager, gson));
    }

    public void start() {
        System.out.println("Http-сервер запущен на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Http-сервер остановлен");
    }

    public static Gson getGson() {
        return gson;
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        new HttpTaskServer(manager).start();
    }
}