package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String taskName, String description) {
        super(taskName, description, Status.NEW, Duration.ZERO, LocalDateTime.now());
    }

    public Epic(Integer id, String taskName, String description) {
        super(id, taskName, description, Status.NEW, Duration.ZERO, LocalDateTime.now());
    }

    public Epic(String taskName, String description, Duration duration, LocalDateTime localDateTime) {
        super(taskName, description, Status.NEW, duration, localDateTime);
    }

    @Override
    public LocalDateTime getEndTime() {
//        Optional<Subtask> endTime;
//        try {
//            endTime = subtasks.values().stream().max(Comparator.comparing(Task::getEndTime));
//}catch (NotFoundException ex){
//    new NotFoundException(ex);
//}
//
        return endTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    public void addTask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateStatus();
    }

    public void removeTask(Subtask subtask) {
        subtasks.remove(subtask.getId());
        updateStatus();
    }

    public void removeSybtasks() {
        subtasks.clear();
        updateStatus();
    }

    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setTaskStatus(Status.NEW);
            return;
        }
        boolean isNew = true;
        boolean isDone = true;
        for (Subtask subtask : subtasks.values()) {
            if (!subtask.getTaskStatus().equals(Status.NEW)) {
                isNew = false;
            }
            if (!subtask.getTaskStatus().equals(Status.DONE)) {
                isDone = false;
            }
        }
        if (isNew) {
            setTaskStatus(Status.NEW);
            return;
        }
        if (isDone) {
            setTaskStatus(Status.DONE);
            return;
        }
        setTaskStatus(Status.IN_PROGRESS);
    }


    @Override
    public TypeOfTask getType() {
        return TypeOfTask.EPIC;
    }
}
