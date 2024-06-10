package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Converter {
    protected static String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getTaskName() + "," + task.getTaskStatus() + "," +
                task.getDescription() + "," + task.getEpicId() + "," +
                task.getDuration().toMinutes() + "," + task.getStartTime();
    }

    protected static Task fromString(String value) {
        if (value.isEmpty()) return null;
        String[] tasks = value.split(",");
        int id = Integer.parseInt(tasks[0]);
        TypeOfTask type = TypeOfTask.valueOf(tasks[1]);
        String name = tasks[2];
        Status status = Status.valueOf(tasks[3]);
        String description = tasks[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(tasks[6]));
        LocalDateTime startTime = LocalDateTime.parse(tasks[7]);

        switch (type) {
            case TASK:
                return new Task(id, name, description, status, duration, startTime);
            case EPIC:
                return new Epic(id, name, description);
            case SUBTASK:
                return new Subtask(id, name, description, status, Integer.parseInt(tasks[5]), duration, startTime);
        }
        return null;
    }
}
