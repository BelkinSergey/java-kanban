package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


public abstract class BaseHttpHandler implements HttpHandler {

    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    protected int processRequest(HttpExchange httpExchange) {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2) {
            return 0;
        } else if (pathParts.length == 3) {
            return 1;
        } else
            return -1;
    }

    protected void sendText(HttpExchange httpExchange, String responseString, int rCode) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            var responseBytes = responseString.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(rCode, responseBytes.length);
            os.write(responseBytes);
        }
        httpExchange.close();
    }

    protected Optional<Integer> getId(HttpExchange httpExchange) {

        String[] parts = httpExchange.getRequestURI().getPath().split("/");
        if (parts.length >= 2) {
            String postIdStr = parts[2];
            try {
                int postId = Integer.parseInt(postIdStr);
                return Optional.of(postId);
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    protected int getIdFromPath(HttpExchange exchange) throws IOException {
        Optional<Integer> idFromPath = getId(exchange);
        return idFromPath.orElse(-1);
    }

    protected boolean checkTaskOverlap(Task newTask) {
        for (Task existingTask : taskManager.getTasks()) {
            if (existingTask.getId() == newTask.getId()) {
                return true;
            }
        }
        return false;
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
                registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting();
        return gsonBuilder.create();
    }

}
