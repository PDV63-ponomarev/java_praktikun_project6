import methods.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

public static FileBackedTasksManager loadFromFile(File file) {
    FileBackedTasksManager manager = new FileBackedTasksManager(file);

    if (!file.exists() || file.length() == 0) {
        return manager; // файла нет — стартуем с пустым менеджером
    }

    FileTask.createTask = manager.creatTask; // см. примечание ниже

    List<String> lines;
    try {
        lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    } catch (IOException e) {
        throw new ManagerSaveException("Ошибка загрузки из файла", e);
    }

    int cursor = 1;  // строка 0 — шапка id,type,name,...
    int maxId = 0;

    // блок задач: всё до первой пустой строки
    while (cursor < lines.size() && !lines.get(cursor).isBlank()) {
        Task task = FileTask.fromString(lines.get(cursor));

        if (task instanceof TaskEpic) {
            manager.creatTask.getEpics().put(task.getTaskID(), (TaskEpic) task);
        } else if (task instanceof TaskSubtask) {
            TaskSubtask sub = (TaskSubtask) task;
            manager.creatTask.getSubtasks().put(sub.getTaskID(), sub);
            // эпик уже восстановлен (в файле эпики идут раньше) — восстанавливаем связь
            TaskEpic epic = manager.creatTask.getEpics().get(sub.getEpicId());
            if (epic != null) {
                epic.addSubtaskId(sub.getTaskID());
            }
        } else {
            manager.creatTask.getTasks().put(task.getTaskID(), task);
        }

        maxId = Math.max(maxId, task.getTaskID());
        cursor++;
    }

    manager.creatTask.setNextId(maxId + 1); // чтобы новые ID не пересеклись со старыми

    // блок истории: строка после пустой
    cursor++;
    if (cursor < lines.size()) {
        for (int id : FileTask.historyFromString(lines.get(cursor))) {
            Task task = findTaskById(manager.creatTask, id);
            if (task != null) {
                manager.creatTask.getHistoryManager().add(task);
            }
        }
    }

    return manager;
}

    @Override
    public void showAllTasks() {
        super.showAllTasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void showTaskFromID() {
        super.showTaskFromID();
        save();
    }

    @Override
    public void creatingNewTask() {
        super.creatingNewTask();
        save();
    }

    @Override
    public void managerUpdateTask() {
        super.managerUpdateTask();
        save();
    }

    @Override
    public void deleteOneTask() {
        super.deleteOneTask();
        save();
    }

    @Override
    public void showHistory() {
        super.showHistory();
    }

    @Override
    public void exit() {
        super.exit();
    }

    @Override
    public void start() {
        super.start();
    }

    public void save(){
        try {
            FileTask.writeFile(file, creatTask);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл", e);
        }
    }

    public static Task findTaskById(CreatTask ct, int id) {
        if (ct.getTasks().containsKey(id))    return ct.getTasks().get(id);
        if (ct.getEpics().containsKey(id))    return ct.getEpics().get(id);
        return ct.getSubtasks().get(id);
    }
}
