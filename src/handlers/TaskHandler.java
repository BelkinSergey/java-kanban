package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ValidationException;
import model.Task;
import service.TaskManager;

import java.nio.charset.StandardCharsets;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try (httpExchange) {
            try {
                switch (httpExchange.getRequestMethod()) {
                    case "GET":
                        if (processRequest(httpExchange) == 0) {
                            sendText(httpExchange, getGson().toJson(taskManager.getTasks()), 200);
                            break;
                        } else if (processRequest(httpExchange) == 1) {
                            sendText(httpExchange, getGson().toJson(taskManager.getTask(getIdFromPath(httpExchange))), 200);
                            break;
                        }
                        if (processRequest(httpExchange) == 1 && getIdFromPath(httpExchange) == -1) {
                            sendText(httpExchange, "Такой задачи нет", 404);
                            break;
                        }
                    case "POST":
                        Task task;
                        try {
                            String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            task = getGson().fromJson(body, Task.class);
                        } catch (JsonSyntaxException exception) {
                            sendText(httpExchange, "Получен некорректный JSON", 400);
                            break;
                        }
                        try {
                            if (checkTaskOverlap(task)) {
                                taskManager.updateTask(task);
                                sendText(httpExchange, "Задача обновлена", 201);
                                break;
                            } else
                                taskManager.createTask(task);
                            sendText(httpExchange, "Задача добавлена", 201);
                            break;
                        } catch (ValidationException e) {
                            sendText(httpExchange, "Задача пересекается с существующей", 406);
                            break;
                        }
                    case "DELETE":
                        if (processRequest(httpExchange) == 0) {
                            taskManager.clearTasks();
                            sendText(httpExchange, "Задачи удалены", 200);
                            break;
                        } else if (processRequest(httpExchange) == 1) {
                            if (getIdFromPath(httpExchange) == -1) {
                                sendText(httpExchange, "Такой задачи нет", 404);
                            } else
                                taskManager.deleteTaskID(getIdFromPath(httpExchange));
                            sendText(httpExchange, "задача удалена", 200);
                            break;
                        }
                    default:
                        sendText(httpExchange, "Введен неверный запрос", 404);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
    }
}

