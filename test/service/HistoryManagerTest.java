package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HistoryManager")
class HistoryManagerTest {

    private HistoryManager historyManager;
    private TaskManager taskManager;
    Task task1, task2;

    Epic epic1, epic2;
    Subtask subtask1, subtask2, subtask3;

    @BeforeEach
    public void addTask() {

        taskManager = new InMemoryTaskManager();
        task1 = taskManager.createTask(new Task("Task 1", "Desc 1", Status.NEW));
        task2 = taskManager.createTask(new Task("Task 2", "Desc 2", Status.IN_PROGRESS));
        epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc epic 1"));
        subtask1 = taskManager.createSubTask(new Subtask("SubTask 1", "Desc sub 1", Status.NEW, epic1));
        subtask2 = taskManager.createSubTask(new Subtask("SubTask 2", "Desc sub 2", Status.NEW, epic1));
        epic2 = taskManager.createEpic(new Epic("Epic 2", "Desc epic 2"));
        subtask3 = taskManager.createSubTask(new Subtask("SubTask 3", "Desc sub 3", Status.NEW, epic1));

        taskManager.getTask(1);
        taskManager.getEpicTask(3);
        taskManager.getSubTask(4);
        taskManager.getSubTask(7);
        taskManager.getEpicTask(3);
    }


    @Test
    @DisplayName("просматриваемые задачи добавляются в конец истории HistoryManager, предыдущая версия удаляется")
    public void shouldSavedNewVersionTasks() {
        final List<Task> history = List.of(task1, subtask1, subtask3, epic1);
        assertEquals(taskManager.getHistory(), history, "истории не совпадают");
    }

    @Test
    @DisplayName("Удаление задачи из начала истории HistoryManager")
    public void shouldDeleteTaskFirst() {
        taskManager.deleteTaskID(1);
        final List<Task> history = List.of(subtask1, subtask3, epic1);
        assertEquals(taskManager.getHistory(), history, "истории не совпадают");
    }

    @Test
    @DisplayName("Удаление задачи из середины истории HistoryManager")
    public void shouldDeleteTaskMiddle() {
        taskManager.deleteTaskID(7);
        final List<Task> history = List.of(task1, subtask1, epic1);
        assertEquals(taskManager.getHistory(), history, "истории не совпадают");
    }

    @Test
    @DisplayName("Удаление задачи из конца истории HistoryManager")
    public void shouldDeleteTaskEnd() {
        taskManager.deleteTaskID(3);
        final List<Task> history = List.of(task1);
        assertEquals(taskManager.getHistory(), history, "истории не совпадают");
    }


}