package methods;

enum EnumStatus{
    NEW,
    IN_PROGRESS,
    DONE
}

enum TaskType{
    TASK,
    EPIC,
    SUBTASK }


public class Task {
    protected  String taskName;
    protected  String taskDescription;
    protected  EnumStatus taskStatus;
    protected  int taskID;
    protected TaskType taskType;

    public Task(String taskName, String taskDescription, EnumStatus status, int taskID, TaskType taskType) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = status;
        this.taskID = taskID;
        this.taskType = taskType;

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
}