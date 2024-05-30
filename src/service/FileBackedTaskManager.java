package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTasks());
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubTasks());

        String textToFile = "";
        for (Task task : allTasks) {
            textToFile = textToFile + Converter.toString(task) + "\n";
        }

        try {
            Files.writeString(file.toPath(), textToFile, CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackedTaskManager();
        try {
            int maxId = 0;
            String stringTasks = Files.readString(file.toPath());
            String[] arrayTasks = stringTasks.split("\n");

            for (String strTask : arrayTasks) {
                Task task = Converter.fromString(strTask);
                if (task != null) {
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                    switch (task.getType()) {
                        case TASK -> fileBackedTaskManager.tasks.put(task.getId(), task);
                        case EPIC -> fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                        case SUBTASK -> {
                            Subtask subtask = (Subtask) task;
                            fileBackedTaskManager.subtasks.put(task.getId(), subtask);
                            if (fileBackedTaskManager.epics.containsKey(subtask.getEpicId())) {
                                Epic epic = fileBackedTaskManager.epics.get(subtask.getEpicId());
                                epic.addTask(subtask);
                                epic.updateStatus();
                            }
                        }
                    }
                }
            }
            fileBackedTaskManager.id = maxId;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла: " + e.getMessage());
        }
        return fileBackedTaskManager;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask createSubTask(Subtask subtask) {
        Subtask newSubtask = super.createSubTask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public Task deleteTaskID(int id) {
        Task task = super.deleteTaskID(id);
        save();
        return task;
    }
}
