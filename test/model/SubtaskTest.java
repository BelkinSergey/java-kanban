package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
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
        Subtask subtask1 = new Subtask("новая подзадача", "описание", Status.NEW, epic,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 0));
        taskManager.createSubTask(subtask1);
        final int id = subtask1.getId();

        Subtask subtask2 = new Subtask(id, "новая подзадача2", "описание2", Status.IN_PROGRESS, epic.getId(),
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 5));
        taskManager.updateSubTask(subtask2);
        Subtask subtask3 = taskManager.getSubTask(id);

        assertEquals(subtask1, subtask2, "задачи не совпадают по id!");
        assertEqualsTask(subtask2, subtask3, "Изменения не применились при обновлении");

        final List<Subtask> subtaskList = taskManager.getSubTasks();

        assertNotNull(subtask1, "задачи не возвращаются!");
        assertEquals(1, subtaskList.size(), "неверное количество задач.");
    }

    @Test
    @DisplayName("При обновлении задачи изменения сохраняются")
    public void shouldEqualsTasksAfterUpdate() {
        Epic epic = new Epic("новый эпик", "описание");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("новая подзадача", "описание", Status.NEW, epic,
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 0));
        taskManager.createSubTask(subtask1);
        final int id = subtask1.getId();

        Subtask subtask2 = new Subtask(id, "новая подзадача2", "описание2", Status.IN_PROGRESS, epic.getId(),
                Duration.ofMinutes(3), LocalDateTime.of(2024, 6, 10, 11, 5));
        taskManager.updateSubTask(subtask2);
        Subtask subtask3 = taskManager.getSubTask(id);

        assertEqualsTask(subtask2, subtask3, "Изменения не применились при обновлении");
    }

    private void assertEqualsTask(Task expected, Task actual, String message) {
        assertEquals(expected.getId(), actual.getId(), message + ", id");
        assertEquals(expected.getTaskName(), actual.getTaskName(), message + ", name");
        assertEquals(expected.getDescription(), actual.getDescription(), message + ", description");
        assertEquals(expected.getDuration(), actual.getDuration(), message + ", duration");
        assertEquals(expected.getStartTime(), actual.getStartTime(), message + ", startTime");
        assertEquals(expected.getEpicId(), actual.getEpicId(), message + ", epicId");
    }

}
