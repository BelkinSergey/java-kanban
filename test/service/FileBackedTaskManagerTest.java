package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File file;
    FileBackedTaskManager taskManager;

    @BeforeEach
    public void beforeEach() throws IOException {
        file = File.createTempFile("tasks", ".csv", null);
        taskManager = new FileBackedTaskManager(file);
        Task task1 = taskManager.createTask(new Task("Task 1", "Desc 1", Status.NEW));
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc epic 1 "));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("SubTask 1", "Desc sub 1", Status.NEW, epic1));
    }

    @AfterEach
    public void afterEaach() {
        if (file != null && file.exists()) {
            boolean isDelete = file.delete();
            System.out.println("Файл успешно удален: " + isDelete);
        }
    }

    @Test
    @DisplayName("Сохраняется состояние при чтении файла")
    public void savesAndLoad() {
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManager.getTasks(), taskManager2.getTasks(), "Списки задач не совпадают");
        assertEquals(taskManager.getEpics(), taskManager2.getEpics(), "Списки эпиков не совпадают");
        assertEquals(taskManager.getSubTasks(), taskManager2.getSubTasks(), "Списки плдзадач не совпадают");
    }

    @Test
    @DisplayName("Назначается корректный id")
    void correctId() {
        taskManager.deleteTaskID(2);
        taskManager.createTask(new Task(" New Task", "New Desc", Status.NEW));
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManager.getTasks(), taskManager2.getTasks(), "Списки задач не совпадают");
        assertEquals(taskManager.getEpics(), taskManager2.getEpics(), "Списки эпиков не совпадают");
        assertEquals(taskManager.getSubTasks(), taskManager2.getSubTasks(), "Списки плдзадач не совпадают");

    }

}