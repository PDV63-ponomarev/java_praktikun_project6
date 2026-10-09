package methods;

import java.util.HashMap;
import java.util.Scanner;

public class ShowTask {
    private Scanner scanner = new Scanner(System.in);
    private CreatTask createTask;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, TaskEpic> epics;
    private HashMap<Integer, TaskSubtask> subtasks;
    private HistoryManager historyManager;

    public ShowTask(CreatTask createTask) {
        this.createTask = createTask;
        this.tasks = createTask.getTasks();
        this.epics = createTask.getEpics();
        this.subtasks = createTask.getSubtasks();
        this.historyManager = createTask.getHistoryManager();
    }

    public void showAllTasks(){
        if (tasks.isEmpty() && epics.isEmpty() && subtasks.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            showAllSimpleTasks();
            showAllEpics();
            showAllSubtasks();
        }
    }

    private void showAllSimpleTasks() {
        System.out.println("Задачи:");
        HashMap<Integer, Task> tasks = createTask.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("Нет обычных задач");
            return;
        }
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    private void showAllEpics() {
        System.out.println("Большие задачи:");
        HashMap<Integer, TaskEpic> epics = createTask.getEpics();
        if (epics.isEmpty()) {
            System.out.println("Нет больших задач");
            return;
        }
        for (TaskEpic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    private void showAllSubtasks() {
        System.out.println("Подзадачи:");
        HashMap<Integer, TaskSubtask> subtasks = createTask.getSubtasks();
        if (subtasks.isEmpty()) {
            System.out.println("Нет подзадач");
            return;
        }
        for (TaskSubtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    public void showTaskFromID(){
        System.out.println("Введите ID задачи:");

        String input = scanner.nextLine().trim();

        HashMap<Integer, Task> tasks = createTask.getTasks();
        HashMap<Integer, TaskEpic> epics = createTask.getEpics();
        HashMap<Integer, TaskSubtask> subtasks = createTask.getSubtasks();

        if (input.isEmpty()) {
            System.out.println("Ошибка: ввод не может быть пустым");
            return;
        }

        try {
            int id = Integer.parseInt(input);

            while (true) {
                Task task = tasks.get(id);
                TaskEpic epic = epics.get(id);
                TaskSubtask subtask = subtasks.get(id);

                if (task != null) {
                    System.out.println(task);
                    historyManager.add(task);
                    break;
                } else if (epic != null) {
                    System.out.println(epic);
                    historyManager.add(epic);
                    break;
                } else if (subtask != null) {
                    System.out.println(subtask);
                    historyManager.add(subtask);
                    break;
                } else {
                    System.out.println("Задача с ID " + id + " не найдена");
                    break;
                }
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Ошибка: нужно ввести число");
        }
    }
}
