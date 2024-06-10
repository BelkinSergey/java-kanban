package service;

import exceptions.ValidationException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;

    protected int id = 0;

    public ArrayList<Task> getPriorityTasks() {
        return new ArrayList<>(priorityTasks);
    }

    protected TreeSet<Task> priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        tasks.keySet().forEach(historyManager::remove);
        tasks.values().forEach(priorityTasks::remove);
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        priorityTasks.remove(subtasks.values());
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subtasks.keySet().forEach(historyManager::remove);
        epics.values().forEach(epic -> {
            epic.removeSybtasks();
            updateEpicInfo(epic);
        });
        subtasks.values().forEach(priorityTasks::remove);
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

    private boolean checkTasksOverlap(Task task, Task existTask) {
        return !(task.getStartTime().isAfter(existTask.getEndTime()) || task.getEndTime().isBefore(existTask.getStartTime()));
    }

    private void checkTaskTime(Task task) {
        List<Task> priorityTasks = getPriorityTasks();
        priorityTasks.stream()
                .filter(existTask -> existTask.getId() != task.getId())
                .filter(existTask -> checkTasksOverlap(task, existTask))
                .findFirst()
                .ifPresent(existTask -> {
                    throw new ValidationException("Задача " + task.getTaskName() +
                            " пересекается с задачей " + existTask.getTaskName());
                });
    }

    // Создание задачи
    @Override
    public Task createTask(Task task) {
        task.setTaskId(++id);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            checkTaskTime(task);
            priorityTasks.add(task);
        }
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
        updateEpicInfo(epic);
        if (subtask.getStartTime() != null) {
            checkTaskTime(subtask);
            priorityTasks.add(subtask);
        }
        return subtask;
    }

    // Обновление
    @Override
    public void updateTask(Task task) {
        Task existingTask = tasks.get(task.getId());
        if (existingTask != null) {
            checkTaskTime(task);
            priorityTasks.remove(existingTask);
            priorityTasks.add(task);
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) return;
        savedEpic.setTaskName(epic.getTaskName());
        savedEpic.setDescription(epic.getDescription());
        updateEpicInfo(epic);
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        Subtask existingSubTask = subtasks.get(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicTask(subtask.getEpicId());
        epic.addTask(subtask);
        checkTaskTime(subtask);
        priorityTasks.remove(existingSubTask);
        priorityTasks.add(subtask);
        updateEpicInfo(epic);
    }

    // Удаление по индефикатору
    @Override
    public Task deleteTaskID(int id) {
        Task removeTask = null;
        if (tasks.containsKey(id)) {
            removeTask = tasks.remove(id);
            priorityTasks.remove(removeTask);
        }
        if (epics.containsKey(id)) {
            removeTask = epics.remove(id);
            ArrayList<Subtask> subtasksOfEpic = ((Epic) removeTask).getSubtasks();
            for (Subtask subtask : subtasksOfEpic) {
                subtasks.remove(subtask.getId());
                priorityTasks.remove(subtask);
                historyManager.remove(subtask.getId());
                ((Epic) removeTask).removeTask(subtask);
            }
            priorityTasks.remove(removeTask);
        }
        if (subtasks.containsKey(id)) {
            removeTask = subtasks.remove(id);
            Epic epic = getEpicTask(((Subtask) removeTask).getEpicId());
            epic.removeTask((Subtask) removeTask);
            epic.updateStatus();
            priorityTasks.remove(removeTask);
            updateEpicInfo(epic);
        }
        historyManager.remove(id);
        return removeTask;
    }

    @Override
    public ArrayList<Subtask> getSubTasks(Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }

    public void updateEpicInfo(Epic epic) {
        LocalDateTime minTime = LocalDateTime.MAX;
        LocalDateTime maxTime = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(0);

        epic.updateStatus();

        for (Subtask subTask : epic.getSubtasks()) {
            if (subTask.getStartTime().isBefore(minTime)) {
                minTime = subTask.getStartTime();
            }

            if (subTask.getEndTime().isAfter(maxTime)) {
                maxTime = subTask.getEndTime();
            }
            duration = duration.plus(subTask.getDuration());

            epic.setStartTime(minTime);
            epic.setEndTime(maxTime);
            epic.setDuration(duration);
        }
    }
}
