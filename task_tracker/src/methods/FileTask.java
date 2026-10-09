package methods;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


public class FileTask {

    public static CreatTask createTask;

    public static void writeFile(File file, CreatTask createTask) throws IOException {

        try (Writer fileWriter = new FileWriter(file, false)) {

            fileWriter.write("id,type,name,status,description,epic" + "\n");

            for (Task task : createTask.getAllTasks()) {
                fileWriter.write(writeTask(task) + "\n");
            }

            fileWriter.write("\n");

            fileWriter.write(historyToString(createTask.getHistoryManager()));
        }
    }

    static String writeTask(Task task){
        String epicId = "";
        if (task instanceof TaskSubtask) {
            epicId = String.valueOf(((TaskSubtask) task).getEpicId());
        }
        return String.format("%d,%s,%s,%s,%s,%s",
                task.taskID, task.taskType, task.taskName, task.taskStatus, task.taskDescription, epicId);
    }

    public static Task fromString(String value) {
        String[] f = value.split(",", -1); // -1: не выбрасывать пустые поля в конце

        int id = Integer.parseInt(f[0].trim());
        TaskType type = TaskType.valueOf(f[1].trim());
        String name = f[2].trim();
        EnumStatus status = EnumStatus.valueOf(f[3].trim());
        String description = f[4].trim();
        String epicField   = f[5].trim();

        switch (type) {
            case TaskType.TASK:
                return new Task(name, description, status, id, type);
            case TaskType.EPIC:
                return new TaskEpic(name, description, status, id, type);
            case TaskType.SUBTASK:
                int epicId = Integer.parseInt(epicField);
                TaskEpic epic = createTask.getEpics().get(epicId);
                String epicName = (epic != null) ? epic.getTaskName() : "";
                return new TaskSubtask(name, description, status, id, epicId, epicName, type);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    static String historyToString(HistoryManager historyManager){

        StringBuilder sb = new StringBuilder();

        List<Task> history = historyManager.getHistory();

        for (int i = 0; i < history.size(); i++){
            if (i > 0) {
                sb.append(",");
            }
            sb.append(history.get(i).getTaskID());
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();
        if (value == null || value.isBlank()) {
            return ids;
        }
        for (String s : value.split(",")) {
            ids.add(Integer.parseInt(s.trim()));
        }
        return ids;
    }
}
