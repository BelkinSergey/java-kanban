
package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ValidationException;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try (httpExchange) {
            try {
                int process = processRequest(httpExchange);
                switch (httpExchange.getRequestMethod()) {
                    case "GET":
                        if (process == 0) {
                            sendText(httpExchange, getGson().toJson(taskManager.getSubTasks()), 200);
                            break;
                        } else if (process == 1) {
                            sendText(httpExchange, getGson().toJson(taskManager.getSubTask(getIdFromPath(httpExchange))), 200);
                            break;
                        }
                        if (process == 1 && getIdFromPath(httpExchange) == -1) {
                            sendText(httpExchange, "Такой задачи нет", 404);
                            break;
                        }
                    case "POST":
                        Subtask subTask;
                        try {
                            String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            subTask = getGson().fromJson(body, Subtask.class);
                        } catch (JsonSyntaxException exception) {
                            sendText(httpExchange, "Получен некорректный JSON", 400);
                            break;
                        }
                        try {
                            if (checkTaskOverlap(subTask)) {
                                taskManager.updateSubTask(subTask);
                                sendText(httpExchange, "Задача обновлена", 201);
                                break;
                            } else
                                taskManager.createSubTask(subTask);
                            sendText(httpExchange, "Задача добавлена", 201);
                            break;
                        } catch (ValidationException e) {
                            sendText(httpExchange, "Задача пересекается с существующей", 406);
                            break;
                        }
                    case "DELETE":
                        if (process == 0) {
                            taskManager.clearSubTasks();
                            sendText(httpExchange, "Задачи удалены", 200);
                            break;
                        } else if (process == 1) {
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
            } catch (IOException e) {
                System.out.println();
            }
        }
    }
}