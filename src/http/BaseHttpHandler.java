package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        sendText(h, "{\"Not Found\"}", 404);
    }

    protected void sendHasOverlaps(HttpExchange h) throws IOException {
        sendText(h, "Not Acceptable", 406);
    }

    protected void sendServerError(HttpExchange h, Exception e) throws IOException {
        sendText(h, "Internal Server Error" + e.getMessage(), 500);
    }
}