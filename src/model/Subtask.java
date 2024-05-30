package model;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String taskName, String description, Status taskStatus, Epic epic) {
        super(taskName, description, taskStatus);
        this.epicId = epic.getId();
    }

    public Subtask(String taskName, String description, Status taskStatus, Integer epicId) {
        super(taskName, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(int taskId, String taskName, String description, Status taskStatus, Integer epicId) {
        super(taskId, taskName, description, taskStatus);
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
