package service;

import model.Status;
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

    @BeforeEach
    public void addTask(){
//        historyManager = Managers.getDefaultHistory();
        taskManager = new InMemoryTaskManager();
    }


    @Test
    @DisplayName("добавляемые в HistoryManager задачи сохраняют предыдущую версию задачи")
    public void shouldSavedAllVersionTasks(){
        Task task = new Task("Task", "Desc", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTaskOfId(task.getTaskId());

        Task taskUpdate = new Task(task.getTaskId(), "Task update", "Desc update", Status.IN_PROGRESS);
        taskManager.updateTask(taskUpdate);

        taskManager.getTaskOfId(task.getTaskId());

        final List<Task> history = taskManager.getHistory();

        assertNotNull(history, "история пустая.");
        assertEquals(2, history.size(), "история из двух тасков.");
        assertEqualsTask(history.get(0), history.get(1), "таски не совпадают по");
    }
    private void assertEqualsTask(Task expected, Task task, String message) {
        Boolean isNotEquals = expected.getTaskId() != task.getTaskId() ||
                !expected.getTaskName().equals(task.getTaskName()) ||
                !expected.getDescription().equals(task.getDescription()) ||
                !expected.getTaskStatus().equals(task.getTaskStatus());

        assertTrue(isNotEquals, "Tasks equals");

    }

}