package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task")
class TaskTest {

    private TaskManager taskManager;

    @BeforeEach
    public void addTask() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("экземпляры класса Task равны друг другу, если равен их id")
    public void shouldTwoTasksEqualsIdAreEquals() {
        Task task1 = new Task("новая задача", "описание", Status.NEW,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 0));
        taskManager.createTask(task1);
        final int id = task1.getId();

        Task task2 = new Task(id, "новая задача2", "описание2", Status.IN_PROGRESS,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 0));
        taskManager.updateTask(task2);

        assertNotNull(task2, "задача не найдена");
        assertEquals(task1, task2, "задачи не совпадают по id!");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "задачи не возвращаются!");
        assertEquals(1, tasks.size(), "неверное количество задач");
        assertEquals(task1, tasks.get(0));
    }
}
