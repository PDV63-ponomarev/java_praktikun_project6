package methods;

import java.util.ArrayList;
import java.util.List;

public class TaskEpic extends Task{
    private List<Integer> subtaskIds;

    public TaskEpic(String taskName, String taskDescription, EnumStatus status, int taskID, TaskType taskType) {
        super(taskName, taskDescription, status, taskID, taskType);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void updateStatus(List<TaskSubtask> subtasks) {
        if (subtaskIds.isEmpty()) {
            this.taskStatus = EnumStatus.NEW;
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (int id : subtaskIds) {
            for (TaskSubtask subtask : subtasks) {
                if (subtask.getTaskID() == id) {
                    if (!subtask.getTaskStatus().equals(EnumStatus.NEW)) {
                        allNew = false;
                    }
                    if (!subtask.getTaskStatus().equals(EnumStatus.DONE)) {
                        allDone = false;
                    }
                    break;
                }
            }
        }

        if (allNew) {
            this.taskStatus = EnumStatus.NEW;
        } else if (allDone) {
            this.taskStatus = EnumStatus.DONE;
        } else {
            this.taskStatus = EnumStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return String.format("Задача {Название: %s; Описание: %s; Статус: %s; ID: %d; Подзадачи: %s}",
                taskName,
                taskDescription,
                taskStatus,
                taskID,
                subtaskIds
        );
    }
}