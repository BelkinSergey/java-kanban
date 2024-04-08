package model;

public class Subtask extends Task {

    private Epic epic;

    public Subtask(String taskName, String description, Status taskStatus, Epic epic) {
        super(taskName, description, taskStatus);
        this.epic = epic;
    }
    public Subtask(int taskId, String taskName, String description, Status taskStatus, Epic epic) {
        super(taskId, taskName, description, taskStatus);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

}
