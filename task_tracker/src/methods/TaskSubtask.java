package methods;

public class TaskSubtask extends Task{
    private int epicId;
    private String epicName;

    public TaskSubtask(String taskName, String taskDescription, EnumStatus status, int taskID,
                       int epicId, String epicName, TaskType taskType) {
        super(taskName, taskDescription, status, taskID, taskType);
        this.epicId = epicId;
        this.epicName = epicName;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("Задача {Название: %s; Описание: %s; Статус: %s; ID: %d; Глобальная задача: %s (%d)}",
                taskName,
                taskDescription,
                taskStatus,
                taskID,
                epicName,
                epicId);
    }
}