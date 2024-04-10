package model;

public class Task {
    private int id;
    private String taskName;
    private String description;
    private Status taskStatus;
    public Task(int taskId, String taskName, String description, Status taskStatus) {
        this.id = taskId;
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(String taskName, String description, Status taskStatus) {
        this.taskName = taskName;
        this.description = description;
        this.taskStatus = taskStatus;
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


    public int getTaskId() {
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
                ", description: '" + description + "'}";
    }
}
