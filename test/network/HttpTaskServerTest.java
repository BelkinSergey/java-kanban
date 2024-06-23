package network;

import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static handlers.BaseHttpHandler.getGson;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    TaskManager manager = Managers.getDefault();

    HttpTaskServer taskServer = new HttpTaskServer(manager);


    @BeforeEach
    public void setUp() {
        manager.clearTasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void canTaskCreated() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        String taskJson = getGson().toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getTasks();
        System.out.println(tasksFromManager);
        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя 1", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getTask(1).toString(), getGson().fromJson(response.body(), Task.class).toString(),
                "Задачи не совпадают");
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.createTask(task);
        manager.createTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getTasks();
        List<Task> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.createTask(task);
        manager.createTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getTasks();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getTasks();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    public void canEpicCreated() throws IOException, InterruptedException {

        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        String taskJson = getGson().toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> tasksFromManager = manager.getEpics();
        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя 1", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание");
//        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpicTask(1).toString(), getGson().fromJson(response.body(), Epic.class).toString(),
                "Задачи не совпадают");
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Epic epic2 = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        manager.createEpic(epic2);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        Subtask subTask1 = new Subtask("Имя", "Описание", Status.NEW, 2, Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0));
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> expectedTasks = manager.getEpics();
        List<Task> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void getSubtaskByEpicId() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        Subtask subTask1 = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0));
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> expectedTasks = manager.getSubTasks(manager.getEpicTask(1));
        List<Subtask> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Epic epic2 = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        manager.createEpic(epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> expectedTasks = manager.getEpics();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> expectedTasks = manager.getEpics();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    public void canSubTaskCreated() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        String taskJson = getGson().toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> tasksFromManager = manager.getSubTasks();
        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Имя", tasksFromManager.getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    void getSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubTask(2).toString(), getGson().fromJson(response.body(), Subtask.class).toString(),
                "Задачи не совпадают");
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        Subtask subTask1 = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0));
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> expectedTasks = manager.getSubTasks();
        List<Task> actualTasks = getGson().fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void deleteSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        Subtask subTask1 = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2025, 1, 1, 2, 0));
        manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> expectedTasks = manager.getSubTasks();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void deleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Имя 1", "Описание", Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        manager.createEpic(epic);
        Subtask subTask = new Subtask("Имя", "Описание", Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> expectedTasks = manager.getSubTasks();
        assertEquals(expectedTasks.size(), 0);
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.createTask(task);
        manager.createTask(task1);
        manager.getTask(1);
        manager.getTask(2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> expectedTasks = manager.getHistory();
        assertEquals(expectedTasks.size(), 2);
    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        Task task = new Task("Имя 1", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2019, 1, 3, 2, 0));
        Task task1 = new Task("Имя 2", "Описание", Status.NEW, Duration.ofMinutes(3), LocalDateTime.of(2020, 1, 3, 2, 0));
        manager.createTask(task);
        manager.createTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        TreeSet<Task> expectedTasks = manager.getPriorityTasks();
        assertEquals(expectedTasks.size(), 2);
    }
}