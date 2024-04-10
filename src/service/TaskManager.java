package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {


    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;


    int id = 0;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }



    // Получение списка всех задач
    public ArrayList<Task> getTasks(){
        return new ArrayList<>(tasks.values());
    }
    public ArrayList<Epic> getEpics(){
        return new ArrayList<>(epics.values());
    }
    public ArrayList<Subtask> getSubTasks(){
        return new ArrayList<>(subtasks.values());
    }


    // Удаление всех задач
    public void clearTasks(){
        tasks.clear();
    }
    public void clearEpics(){
        epics.clear();
        subtasks.clear();
    }
    public void clearSubTasks(){
        for (Subtask subtask: subtasks.values()){
            Epic epic = subtask.getEpic();
            epic.removeTask(subtask);
            epic.updateStatus();
        }
        subtasks.clear();
    }


// Получение по идентификатору
    public Task getTaskOfId(int id){
        if(tasks.containsKey(id)){
            return tasks.get(id);
        }
        return null;
    }
    public Epic getEpicTaskOfId(int id){
        if(epics.containsKey(id)){
            return epics.get(id);
        }
        return null;
    }
    public Subtask getSubTaskOfId(int id){
        if(subtasks.containsKey(id)){
            return subtasks.get(id);
        }
        return null;
    }


    // Создание задачи
    public Task createTask(Task task){
        task.setTaskId(++id);
        tasks.put(task.getTaskId(), task);
        return task;
    }
    public Epic createEpic(Epic epic){
        epic.setTaskId(++id);
        epics.put(epic.getTaskId(), epic);
        return epic;
    }
    public Subtask createSubTask(Subtask subtask){
        subtask.setTaskId(++id);
        subtasks.put(subtask.getTaskId(), subtask);
        Epic epic = epics.get(subtask.getEpic().getTaskId());
        epic.addTask(subtask);
        epic.updateStatus();
        return subtask;
    }

    // Обновление
    public void updateTask(Task task){
        tasks.put(task.getTaskId(), task);
    }
    public void updateEpic(Epic epic){
        Epic savedEpic = epics.get(epic.getTaskId());
        if (savedEpic == null) return;
        savedEpic.setTaskName(epic.getTaskName());
        savedEpic.setDescription(epic.getDescription());
    }
    public void updateSubTask(Subtask subtask){
        subtasks.put(subtask.getTaskId(), subtask);
        subtask.getEpic().addTask(subtask);
    }

// Удаление по индефикатору
    public Task deleteTaskID(int id){
        Task removeTask = null;
        if (tasks.containsKey(id)) {
            removeTask = tasks.remove(id);
        }
        if (epics.containsKey(id)) {
            removeTask = epics.remove(id);
            ArrayList<Subtask> subtasksOfEpic = ((Epic)removeTask).getSubtasks();
            for (Subtask subtask: subtasksOfEpic){
                subtasks.remove(subtask.getTaskId());
                ((Epic)removeTask).removeTask(subtask);
            }
        }
        if (subtasks.containsKey(id)) {
            removeTask = subtasks.remove(id);
            Epic epic = ((Subtask)removeTask).getEpic();
            epic.removeTask((Subtask) removeTask);
            epic.updateStatus();
        }
        return  removeTask;
    }

    public ArrayList<Subtask> getSubTasks(Epic epic){
        return new ArrayList<>(epic.getSubtasks());
    }

}
