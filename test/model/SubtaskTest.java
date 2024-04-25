package model;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Subtask")
class SubtaskTest {
    private TaskManager taskManager;

    @BeforeEach
    public void addTask() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("наследники класса Task равны друг другу, если равен их id")
    public void shouldEqualsSubtasks() {
        Epic epic = new Epic("новый эпик", "описание");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("новая подзадача", "описание", Status.NEW, epic);
        taskManager.createSubTask(subtask1);
        final int id = subtask1.getTaskId();

        Subtask subtask2 = new Subtask(id, "новая подзадача2", "описание2", Status.IN_PROGRESS, epic);
        taskManager.updateSubTask(subtask2);

        assertEquals(subtask1, subtask2, "задачи не совпадают!");

        final List<Subtask> subtaskList = taskManager.getSubTasks();

        assertNotNull(subtask1, "задачи не возвращаются!");
        assertEquals(1, subtaskList.size(), "неверное количество задач.");
    }
}