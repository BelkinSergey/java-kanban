package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Managers")
class ManagersTest {

    private TaskManager taskManager;


    @BeforeEach
    public void addTask() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры")
    public void shouldInitializedInstancesNotNull() {
        assertNotNull(taskManager, "экземпляр класса не проинициализирован");
    }


    @Test
    @DisplayName("InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id")
    public void shouldAddAllTypeTasks() {
        Task task = new Task("новая задача", "описание", Status.NEW);
        taskManager.createTask(task);

        Epic epic = new Epic("новый эпик", "описание");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("новая подзадача", "описание", Status.NEW, epic);
        taskManager.createSubTask(subtask);

        assertEquals(1, taskManager.getTasks().size(), "количество задач не совпадает");
        assertEquals(1, taskManager.getEpics().size(), "количество задач не совпадает");
        assertEquals(1, taskManager.getSubTasks().size(), "количество задач не совпадает");

        assertNotNull(taskManager.getTask(1), "задача не найдена");
        assertNotNull(taskManager.getEpicTask(2), "задача не найдена");
        assertNotNull(taskManager.getSubTask(3), "задача не найдена");

        assertEquals(task, taskManager.getTask(1), "задачи не совпадают");
        assertEquals(epic, taskManager.getEpicTask(2), "задачи не совпадают");
        assertEquals(subtask, taskManager.getSubTask(3), "задачи не совпадают");
    }

    @Test
    @DisplayName("проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    public void shouldTasksIdDoNotConflictInMemoryTaskManager() {
        Task task1 = new Task("новая задача", " описание", Status.NEW);
        taskManager.createTask(task1);

        Task task2 = new Task("новая задача2", "описание", Status.IN_PROGRESS);
        taskManager.createTask(task2);

        Task task3 = new Task(2, "новая задача3", "описание", Status.NEW);
        taskManager.updateTask(task3);

        assertEquals(task2, task3, "задачи не совпадают");
        assertEquals(2, taskManager.getTasks().size(), "количество задач не совпадает");
    }

    @Test
    @DisplayName("неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    public void shouldTasksUnchangedWhenAddingToInMemoryTaskManager() {
        Task task = new Task("новая задача", "описание", Status.NEW);
        taskManager.createTask(task);
        Task task1 = taskManager.getTask(1);

        assertEquals(task.getTaskName(), task1.getTaskName(), "поля не совпадают");
        assertEquals(task.getDescription(), task1.getDescription(), "поля не совпадают");
        assertEquals(task.getTaskStatus(), task1.getTaskStatus(), "поля не совпадают");
        assertEquals(task.getId(), task1.getId(), "поля не совпадают");
    }
}



