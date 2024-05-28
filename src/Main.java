import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.TaskManager;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = new FileBackedTaskManager(new File("resources/tasks.csv"));
        // Создание
        Task task1 = taskManager.createTask(new Task("Задача 1", "Desc 1", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Task 2", "Desc 2", Status.IN_PROGRESS));
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc epic 1"));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("SubTask 1", "Desc sub 1", Status.NEW, epic1));

        Subtask subtask2 = taskManager.createSubTask(new Subtask("SubTask 2", "Desc sub 2", Status.NEW, 3));
        Epic epic2 = taskManager.createEpic(new Epic("Epic 2", "Desc epic 2"));
        Subtask subtask3 = taskManager.createSubTask(new Subtask("SubTask 3", "Desc sub 3", Status.NEW, 3));

        TaskManager taskManager2 = FileBackedTaskManager.loadFromFile(new File("resources/tasks.csv"));
        taskManager2.deleteTaskID(3);


        System.out.println(taskManager2.getTasks());
        System.out.println(taskManager2.getEpics());
        System.out.println(taskManager2.getSubTasks());


    }
}
