package service;

import exceptions.ValidationException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;


    @Test
    @DisplayName("InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id")
    public void shouldAddAllTypeTasks() {
        Task task = new Task("новая задача", "описание", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 0));
        taskManager.createTask(task);

        Epic epic = new Epic("новый эпик", "описание");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("новая подзадача", "описание", Status.NEW, epic,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 5));
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
        Task task1 = new Task("новая задача", " описание", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 10));
        taskManager.createTask(task1);

        Task task2 = new Task("новая задача2", "описание", Status.IN_PROGRESS,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 15));
        taskManager.createTask(task2);

        Task task3 = new Task(2, "новая задача3", "описание", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 20));
        taskManager.updateTask(task3);

        assertEquals(task2, task3, "задачи не совпадают");
        assertEquals(2, taskManager.getTasks().size(), "количество задач не совпадает");
    }

    @Test
    @DisplayName("неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    public void shouldTasksUnchangedWhenAddingToInMemoryTaskManager() {
        Task task = new Task("новая задача", "описание", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 25));
        taskManager.createTask(task);
        Task task1 = taskManager.getTask(1);

        assertEquals(task.getTaskName(), task1.getTaskName(), "поля не совпадают");
        assertEquals(task.getDescription(), task1.getDescription(), "поля не совпадают");
        assertEquals(task.getTaskStatus(), task1.getTaskStatus(), "поля не совпадают");
        assertEquals(task.getId(), task1.getId(), "поля не совпадают");
    }


    @Test
    @DisplayName("Проверка корректности статуса эпика")
    public void shouldStatusEpicIsCorrect() {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "описание 1", Status.NEW, epic,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 5));
        Subtask subtask2 = new Subtask("Подзадача 1", "описание 1", Status.NEW, epic,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 10));
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        assertEquals(epic.getTaskStatus(), Status.NEW, "Статус эпика должен быть NEW");

        subtask1.setTaskStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subtask1);
        assertEquals(epic.getTaskStatus(), Status.IN_PROGRESS, "Статус эпика должен быть IN_PROGRESS");

        subtask1.setTaskStatus(Status.DONE);
        taskManager.updateSubTask(subtask1);
        assertEquals(epic.getTaskStatus(), Status.IN_PROGRESS, "Статус эпика должен быть IN_PROGRESS");


        subtask2.setTaskStatus(Status.DONE);
        taskManager.updateSubTask(subtask2);
        assertEquals(epic.getTaskStatus(), Status.DONE, "Статус эпика должен быть DONE");

    }

    @Test
    @DisplayName("Корректность расчета пересечения интервалов (пересечение интервалов)")
    public void shouldCheckTasksOverlapIsNotCorrect() throws ValidationException {
        Task task1 = new Task("задача1", "описание1", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 0));

        Task task2 = new Task("задача2", "описание2", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 1));


        assertThrows(
                ValidationException.class, () -> {
                    taskManager.createTask(task1);
                    taskManager.createTask(task2);
                }, "Не сработало исключение ValidationException");

    }

    @Test
    @DisplayName("Корректность расчета пересечения интервалов (правильные интервалы)")
    public void shouldCheckTasksOverlapIsCorrect() throws ValidationException {
        Task task1 = new Task("задача1", "описание1", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 0));

        Task task2 = new Task("задача2", "описание2", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 10));


        assertDoesNotThrow(() -> {
                    taskManager.createTask(task1);
                    taskManager.createTask(task2);
                }, "Не сработало исключение ValidationException"
        );

    }
}

