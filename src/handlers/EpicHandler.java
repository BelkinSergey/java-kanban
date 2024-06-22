package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ValidationException;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try (httpExchange) {
            try {
                switch (httpExchange.getRequestMethod()) {
                    case "GET":
                        if (processRequest(httpExchange) == 0) {
                            sendText(httpExchange, getGson().toJson(taskManager.getEpics()), 200);
                            break;
                        } else if (processRequest(httpExchange) == 1) {
                            sendText(httpExchange, getGson().toJson(taskManager.getEpicTask(getIdFromPath(httpExchange))), 200);
                            break;
                        } else if (processRequest(httpExchange) == -1) {
                            sendText(httpExchange, getGson().toJson(taskManager.getSubTasks(taskManager.getEpicTask(getIdFromPath(httpExchange)))), 200);
                            break;
                        }
                        if (processRequest(httpExchange) == 1 && getIdFromPath(httpExchange) == -1) {
                            sendText(httpExchange, "Такой задачи нет", 404);
                            break;
                        }
                    case "POST":
                        Epic epic;
                        try {
                            String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            epic = getGson().fromJson(body, Epic.class);
                        } catch (JsonSyntaxException exception) {
                            sendText(httpExchange, "Получен некорректный JSON", 400);
                            break;
                        }
                        try {
                            if (checkTaskOverlap(epic)) {
                                taskManager.updateEpic(epic);
                                sendText(httpExchange, "Задача обновлена", 201);
                                break;
                            } else
                                taskManager.createEpic(epic);
                            sendText(httpExchange, "Задача добавлена", 201);
                            break;
                        } catch (ValidationException e) {
                            sendText(httpExchange, "Задача пересекается с существующей", 406);
                            break;
                        }
                    case "DELETE":
                        if (processRequest(httpExchange) == 0) {
                            taskManager.clearEpics();
                            sendText(httpExchange, "Задачи удалены", 200);
                            break;
                        } else if (processRequest(httpExchange) == 1) {
                            taskManager.deleteTaskID(getIdFromPath(httpExchange));
                            sendText(httpExchange, "задача удалена", 200);
                            break;
                        }
                        if (processRequest(httpExchange) == 1 && getIdFromPath(httpExchange) == -1) {
                            sendText(httpExchange, "Такой задачи нет", 404);
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
