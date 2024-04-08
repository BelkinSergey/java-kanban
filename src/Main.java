import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();
// Создание
        Task task1 = taskManager.createTask(new Task("Task 1", "Desc 1", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Task 2", "Desc 2", Status.IN_PROGRESS));
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc epic 1"));
        Subtask subtask1 = taskManager.createSubTask(new Subtask("SubTask 1", "Desc sub 1", Status.NEW, epic1));
        Subtask subtask2 = taskManager.createSubTask(new Subtask("SubTask 2", "Desc sub 2", Status.NEW, epic1));
        Epic epic2 = taskManager.createEpic(new Epic("Epic 2", "Desc epic 2"));
        Subtask subtask3 = taskManager.createSubTask(new Subtask("SubTask 3", "Desc sub 3", Status.NEW, epic2));

        System.out.println("\nСозданы:");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());

// Изменение
        Task taskUpdate1 = new Task(task1.getTaskId(), "Task1 update", "Desc1 update", Status.IN_PROGRESS);
        taskManager.updateTask(taskUpdate1);

        Task taskUpdate2 = new Task(task2.getTaskId(), "Task2 update", "Desc2 update", Status.DONE);
        taskManager.updateTask(taskUpdate2);

        Subtask taskFromManager1 = taskManager.getSubTaskOfId(subtask1.getTaskId());
        Subtask subtaskUpdate1 = new Subtask(taskFromManager1.getTaskId(), "SubTask1 update", "desk 1", Status.DONE, epic1);
        taskManager.updateSubTask(subtaskUpdate1);

        Subtask taskFromManager3 = taskManager.getSubTaskOfId(subtask3.getTaskId());
        Subtask subtaskUpdate3 = new Subtask(taskFromManager3.getTaskId(), "SubTask3 update", "desk 3", Status.DONE, epic2);
        taskManager.updateSubTask(subtaskUpdate3);

        epic1.setTaskName("Epic1 update name");

        System.out.println("\nПосле изменения:");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());


// Удаление
        taskManager.deleteTaskID(task1.getTaskId());
        taskManager.deleteTaskID(subtask2.getTaskId());
        taskManager.deleteTaskID(epic2.getTaskId());

        System.out.println("\nПосле удаления:");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());

    }
}
