package tracker.model;

public class TaskSubtask extends Task{
    private int epicId;

    public TaskSubtask(String taskName, String taskDescription, EnumStatus status,
                       int epicId, TaskType taskType) {
        super(taskName, taskDescription, status, taskType);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("Задача {Название: %s; Описание: %s; Статус: %s; ID: %d; Глобальная задача ID: %d}",
                taskName,
                taskDescription,
                taskStatus,
                taskID,
                epicId);
    }
}