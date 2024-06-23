package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String taskName;

    private String description;
    private Duration duration;
    private LocalDateTime startTime;
    private Status taskStatus;


    public Task(String taskName, String description, Status taskStatus, Duration duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String taskName, String description, Status taskStatus, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }


    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }


    public int getId() {
        return id;
    }

    public void setTaskId(int taskId) {
        this.id = taskId;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !getClass().equals(obj.getClass())) return false;
        Task task = (Task) obj;
        return id == task.id;
    }

    @Override
    public String toString() {
        return "Task {" +
                "name: '" + taskName + "'" +
                ", status: '" + taskStatus + "'" +
                ", description: '" + description + "'" +
                ", duration: " + duration.toMinutes() + " min" +
                ", startTime: " + startTime + '}';
    }

    public TypeOfTask getType() {
        return TypeOfTask.TASK;
    }

    public Integer getEpicId() {
        return null;
    }
}
