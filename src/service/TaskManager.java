package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    // Получение списка всех задач
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubTasks();

    // Удаление всех задач
    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    // Получение по идентификатору
    Task getTaskOfId(int id);

    Epic getEpicTaskOfId(int id);

    Subtask getSubTaskOfId(int id);

    // Создание задачи
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubTask(Subtask subtask);

    // Обновление
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(Subtask subtask);

    // Удаление по индефикатору
    Task deleteTaskID(int id);

    ArrayList<Subtask> getSubTasks(Epic epic);
}
