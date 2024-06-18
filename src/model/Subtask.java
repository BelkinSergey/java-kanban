package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String taskName, String description, Status taskStatus, Epic epic, Duration duration, LocalDateTime startTime) {
        super(taskName, description, taskStatus, duration, startTime);
        this.epicId = epic.getId();
    }

    public Subtask(String taskName, String description, Status taskStatus, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(taskName, description, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int taskId, String taskName, String description, Status taskStatus, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(taskId, taskName, description, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    public void setEpic(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

}
