package methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class CreatTask {
    private Scanner scanner = new Scanner(System.in);
    private int nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, TaskEpic> epics = new HashMap<>();
    private HashMap<Integer, TaskSubtask> subtasks = new HashMap<>();
    private HistoryManager historyManager;

    public CreatTask() {
        this.historyManager = new InMemoryHistoryManager();
    }
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }
    public HashMap<Integer, TaskEpic> getEpics() {
        return epics;
    }
    public HashMap<Integer, TaskSubtask> getSubtasks() {
        return subtasks;
    }
    public HistoryManager getHistoryManager() {
        return historyManager;
    }



    public void creatingNewTask() {

        while (true){
            System.out.println("Создание новой задачи: \n" +
                    "Какой типа задачи: \n" +
                    "1 - обычная \n" +
                    "2 - большая \n" +
                    "3 - подзадача \n" +
                    "4 - назад");
            String input = scanner.nextLine();

            try {
                int typeTask = Integer.parseInt(input);
                switch (typeTask) {
                    case 1:
                        createSimpleTask();
                        return;
                    case 2:
                        createEpicTask();
                        return;
                    case 3:
                        createSubtask();
                        return;
                    case 4:
                        return;
                    default:
                        System.out.println("Ошибка: введите число от 1 до 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 4");
                scanner.nextLine();
            }
        }
    }

    public void createSimpleTask() {
        System.out.println("Создание обычной задачи");

        TaskInputData data = createTaskData();

        Task task = new Task(data.getName(), data.getDescription(), data.getStatus(), data.getId(),
                TaskType.TASK);
        tasks.put(task.getTaskID(), task);

        System.out.println("Простая задача успешно создана! ID: " + task.getTaskID());
    }

    public void createEpicTask() {
        System.out.println("Создание большой задачи");

        TaskInputData data = createTaskData();

        TaskEpic epic = new TaskEpic(data.getName(), data.getDescription(), data.getStatus(), data.getId(),
                TaskType.EPIC);
        epics.put(epic.getTaskID(), epic);

        System.out.println("Задача успешно создана! ID: " + epic.getTaskID());
    }

    public void createSubtask() {
        System.out.println("Создание подзадачи");

        if (epics.isEmpty()) {
            System.out.println("Нет больших задач.");
            return;
        }

        System.out.println("Доступные задачи:");
        for (TaskEpic epic : epics.values()) {
            System.out.println("ID: " + epic.getTaskID() + " - " + epic.getTaskName());
        }

        System.out.print("Введите ID задачи: ");
        int epicId = scanner.nextInt();
        scanner.nextLine();

        TaskEpic selectedEpic = epics.get(epicId);
        if (selectedEpic == null) {
            System.out.println("Задача с таким ID не найдена.");
            return;
        }
        String epicName = selectedEpic.getTaskName();

        TaskInputData data = createTaskData();
        TaskSubtask subtask = new TaskSubtask(data.getName(),
                data.getDescription(),
                data.getStatus(),
                data.getId(),
                epicId,
                epicName,
                TaskType.SUBTASK);

        subtasks.put(subtask.getTaskID(), subtask);

        selectedEpic.addSubtaskId(subtask.getTaskID());
        selectedEpic.updateStatus(new ArrayList<>(subtasks.values()));

        System.out.println("Подзадача успешно создана! ID: " + subtask.getTaskID());
        System.out.println("Принадлежит задаче: " + selectedEpic.getTaskName() +
                " (ID: " + selectedEpic.getTaskID() + ")");
    }


    class TaskInputData {
        private String name;
        private String description;
        private EnumStatus status;
        private int id;

        public TaskInputData(String name, String description, EnumStatus status, int id) {
            this.name = name;
            this.description = description;
            this.status = status;
            this.id = id;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public EnumStatus getStatus() { return status; }
        public int getId() { return id; }
    }

    private String safeNextLine() {
        String line = "";
        boolean firstAttempt = true;

        while (line.trim().isEmpty()) {
            if (!firstAttempt) {
                System.out.print("Пожалуйста, введите непустое значение: ");
            }
            line = scanner.nextLine();
            firstAttempt = false;
        }
        return line;
    }

    private TaskInputData createTaskData() {
        System.out.print("Введите название задачи: ");
        String name = safeNextLine();

        System.out.print("Введите описание задачи: ");
        String description = safeNextLine();

        EnumStatus status = getStatusEnum();

        return new TaskInputData(name, description, status, nextId++);
    }


    private EnumStatus getStatusEnum() {
        while (true) {
            try {
                System.out.print("Введите статус (1 - Новая / 2 - В процессе / 3 - Выполнена): ");
                int statusInt = Integer.parseInt(safeNextLine());

                switch (statusInt) {
                    case 1: return EnumStatus.NEW;
                    case 2: return EnumStatus.IN_PROGRESS;
                    case 3: return EnumStatus.DONE;
                    default:
                        System.out.println("Неверный статус! Введите 1, 2 или 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите число 1, 2 или 3.");
            }
        }
    }

    public List<Task> getAllTasks() {
        List<Task> all = new ArrayList<>();
        all.addAll(tasks.values());
        all.addAll(epics.values());
        all.addAll(subtasks.values());
        return all;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

}
