import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = new InMemoryTaskManager();
        // Создание
        Task task1 = taskManager.createTask(new Task("Task 1", "Desc 1", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Task 2", "Desc 2", Status.IN_PROGRESS));
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc epic 1"));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("SubTask 1", "Desc sub 1", Status.NEW, epic1));
        Subtask subtask2 = taskManager.createSubTask(new Subtask("SubTask 2", "Desc sub 2", Status.NEW, epic1));
        Epic epic2 = taskManager.createEpic(new Epic("Epic 2", "Desc epic 2"));
        Subtask subtask3 = taskManager.createSubTask(new Subtask("SubTask 3", "Desc sub 3", Status.NEW, epic1));

        System.out.println("\nСозданы:");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());


        System.out.println("history:  " + taskManager.getHistory());


        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpicTask(3));
        System.out.println(taskManager.getSubTask(4));
        System.out.println(taskManager.getSubTask(7));
        System.out.println("history:  " + taskManager.getHistory());
        System.out.println(taskManager.getSubTask(4));

        System.out.println("history:  " + taskManager.getHistory());
        taskManager.deleteTaskID(epic1.getTaskId());
        System.out.println("history:  " + taskManager.getHistory());


    }
}
