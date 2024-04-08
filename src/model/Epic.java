package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public Epic(String taskName, String description) {
        super(taskName, description, Status.NEW);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>( subtasks.values());
    }

    public void addTask(Subtask subtask){
        subtasks.put(subtask.getTaskId(), subtask);
        updateStatus();
    }
    public void removeTask(Subtask subtask){
        subtasks.remove(subtask.getTaskId());
        updateStatus();
    }

    public void updateStatus(){
        if (subtasks.isEmpty()){
            setTaskStatus(Status.NEW);
            return;
        }
        boolean isNew = true;
        boolean isDone = true;
        for (Subtask subtask: subtasks.values()) {
            if (!subtask.getTaskStatus().equals(Status.NEW)){
                isNew = false;
            }
            if (!subtask.getTaskStatus().equals(Status.DONE)) {
                isDone = false;
            }
        }
        if (isNew){
            setTaskStatus(Status.NEW);
            return;
        }
        if (isDone){
            setTaskStatus(Status.DONE);
            return;
        }
        setTaskStatus(Status.IN_PROGRESS);
    }
}
