package service;

import model.*;

public class Converter {
    protected static String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getTaskName() + "," + task.getTaskStatus() + "," + task.getDescription() + "," + task.getEpicId();
    }

    protected static Task fromString(String value) {

        String[] tasks = value.split(",");
        int id = Integer.parseInt(tasks[0]);
        TypeOfTask type = TypeOfTask.valueOf(tasks[1]);
        String name = tasks[2];
        Status status = Status.valueOf(tasks[3]);
        String description = tasks[4];

        switch (type) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description);
            case SUBTASK:
                return new Subtask(id, name, description, status, Integer.parseInt(tasks[5]));
        }
        return null;
    }
}
