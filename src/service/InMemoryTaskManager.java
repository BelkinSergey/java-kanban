package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {


    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;


    protected int id = 0;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Получение списка всех задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubTasks() {
        return new ArrayList<>(subtasks.values());
    }


    // Удаление всех задач
    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (Subtask subtask : subtasks.values()) {
            Epic epic = getEpicTask(subtask.getEpicId());
            epic.removeTask(subtask);
            epic.updateStatus();
        }
        subtasks.clear();
    }


    // Получение по идентификатору
    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicTask(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubTask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
    }


    // Создание задачи
    @Override
    public Task createTask(Task task) {
        task.setTaskId(++id);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setTaskId(++id);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubTask(Subtask subtask) {
        subtask.setTaskId(++id);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addTask(subtask);
        epic.updateStatus();
        return subtask;
    }

    // Обновление
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) return;
        savedEpic.setTaskName(epic.getTaskName());
        savedEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        getEpicTask(subtask.getEpicId()).addTask(subtask);
    }

    // Удаление по индефикатору
    @Override
    public Task deleteTaskID(int id) {
        Task removeTask = null;
        if (tasks.containsKey(id)) {
            removeTask = tasks.remove(id);
        }
        if (epics.containsKey(id)) {
            removeTask = epics.remove(id);
            ArrayList<Subtask> subtasksOfEpic = ((Epic) removeTask).getSubtasks();
            for (Subtask subtask : subtasksOfEpic) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
                ((Epic) removeTask).removeTask(subtask);
            }
        }
        if (subtasks.containsKey(id)) {
            removeTask = subtasks.remove(id);
            Epic epic = getEpicTask(((Subtask) removeTask).getEpicId());
            epic.removeTask((Subtask) removeTask);
            epic.updateStatus();
        }
        historyManager.remove(id);
        return removeTask;
    }

    @Override
    public ArrayList<Subtask> getSubTasks(Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }

}
