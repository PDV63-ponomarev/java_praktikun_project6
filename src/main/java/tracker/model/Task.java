package tracker.model;


import java.util.Objects;

public class Task {
    protected  String taskName;
    protected  String taskDescription;
    protected  EnumStatus taskStatus;
    protected  int taskID;
    protected TaskType taskType;


    public Task(String taskName, String taskDescription, EnumStatus status, TaskType taskType) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = status;
        this.taskType = taskType;
    }

    public void setTaskID(int taskID) {   // новый сеттер — только для менеджера
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public EnumStatus getTaskStatus() {
        return taskStatus;
    }

    public int getTaskID() {
        return taskID;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskStatus(EnumStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return String.format("Задача {Название: %s; Описание: %s; Статус: %s; ID: %d}",
               taskName,
               taskDescription,
               taskStatus,
               taskID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskID == task.taskID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskID);
    }
}