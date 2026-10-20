package tracker;

import tracker.manager.FileBackedTasksManager;
import tracker.manager.TaskManager;
import tracker.model.*;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static TaskManager manager;

    public static void main(String[] args) {
        manager = FileBackedTasksManager.loadFromFile(new File("tasks.csv"));

        while (true) {
            printMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> printAllTasks();
                case "2" -> deleteAllDialog();
                case "3" -> openTaskById();
                case "4" -> createNewTask();
                case "5" -> updateTaskDialog();
                case "6" -> deleteOneTask();
                case "7" -> printHistory();
                case "8" -> { System.out.println("Выход"); return; }
                default -> System.out.println("Ошибка: введите число от 1 до 8");
            }
        }
    }

    private static void printMenu() {
        System.out.println("""
                Введите команду:
                1 - Получить список задач
                2 - Удалить все задачи
                3 - Открыть задачу
                4 - Создать новую задачу
                5 - Редактировать задачу
                6 - Удалить задачу
                7 - Отобразить историю просмотра задач
                8 - Выход""");
    }

    private static void printAllTasks() {
        for (Task t : manager.getTasks())     System.out.println(t);
        for (TaskEpic e : manager.getEpics()) System.out.println(e);
        for (TaskSubtask s : manager.getSubtasks()) System.out.println(s);
    }

    private static void openTaskById() {
        int id;
        while (true) {
            System.out.print("Введите ID: ");
            String input = scanner.nextLine().trim();
            try {
                id = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ID. Попробуйте снова.");
            }
        }

        Task task = manager.getTask(id);
        if (task == null) task = manager.getEpic(id);
        if (task == null) task = manager.getSubtask(id);
        System.out.println(task == null ? "Задача не найдена" : task);
    }

    private static void createNewTask() {
        String type;
        while (true) {
            System.out.println("Тип: 1 - обычная, 2 - эпик, 3 - подзадача");
            type = scanner.nextLine().trim();
            if (type.equals("1") || type.equals("2") || type.equals("3")) {
                break;
            }
            System.out.println("Неверный тип. Введите 1, 2 или 3.");
        }

        if (type.equals("3") && manager.getEpics().isEmpty()) {
            System.out.println("Нет эпиков — сначала создайте эпик.");
            return;
        }
        System.out.print("Название: ");
        String name = scanner.nextLine().trim();
        System.out.print("Описание: ");
        String desc = scanner.nextLine().trim();
        EnumStatus status = readStatus();

        switch (type) {
            case "1" -> {
                int id = manager.addNewTask(new Task(name, desc, status, TaskType.TASK));
                System.out.println("Создана задача, ID: " + id);
            }
            case "2" -> {
                int id = manager.addNewEpic(new TaskEpic(name, desc, status, TaskType.EPIC));
                System.out.println("Создан эпик, ID: " + id);
            }
            case "3" -> {
                for (TaskEpic e : manager.getEpics()) {
                    System.out.println("ID: " + e.getTaskID() + " - " + e.getTaskName());
                }
                System.out.print("ID эпика: ");
                int epicId = Integer.parseInt(scanner.nextLine().trim());
                int id = manager.addNewSubtask(
                        new TaskSubtask(name, desc, status, epicId ,TaskType.SUBTASK));
                System.out.println(id == -1 ? "Эпик не найден" : "Создана подзадача, ID: " + id);
            }
        }
    }

    private static void updateTaskDialog() {
        System.out.print("ID задачи для обновления: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("1 - Имя, 2 - Описание, 3 - Статус (не для эпика)");
        String choice = scanner.nextLine().trim();

        if (choice.equals("3")) {
            TaskEpic epic = manager.getEpic(id);
            if (epic != null) { System.out.println("Статус эпика меняется автоматически"); return; }
            TaskSubtask sub = manager.getSubtask(id);
            if (sub != null) {
                sub.setTaskStatus(readStatus());
                manager.updateSubtask(sub);
            } else {
                Task task = manager.getTask(id);
                if (task != null) {
                    task.setTaskStatus(readStatus());
                    manager.updateTask(task);
                } else System.out.println("Задача не найдена");
            }
            return;
        }

        System.out.print("Новое значение: ");
        String value = scanner.nextLine().trim();
        Task task = manager.getTask(id);
        if (task != null) {
            if (choice.equals("1")) task.setTaskName(value); else task.setTaskDescription(value);
            manager.updateTask(task);
            return;
        }
        TaskEpic epic = manager.getEpic(id);
        if (epic != null) {
            if (choice.equals("1")) epic.setTaskName(value); else epic.setTaskDescription(value);
            manager.updateEpic(epic);
            return;
        }
        TaskSubtask sub = manager.getSubtask(id);
        if (sub != null) {
            if (choice.equals("1")) sub.setTaskName(value); else sub.setTaskDescription(value);
            manager.updateSubtask(sub);
        } else System.out.println("Задача не найдена");
    }

    private static void deleteOneTask() {
        System.out.print("ID задачи для удаления: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        if (manager.getTask(id) != null)      manager.deleteTask(id);
        else if (manager.getEpic(id) != null) manager.deleteEpic(id);
        else if (manager.getSubtask(id) != null) manager.deleteSubtask(id);
        else { System.out.println("Задача не найдена"); return; }
        System.out.println("Удалено");
    }

    private static void deleteAllDialog() {
        System.out.println("Удалить всё? 1 - Да, 2 - Нет");
        if (scanner.nextLine().trim().equals("1")) {
            manager.deleteTasks();
            manager.deleteEpics();
            manager.deleteSubtasks();
            System.out.println("Все задачи удалены");
        }
    }

    private static void printHistory() {
        for (Task t : manager.getHistory()) System.out.println(t);
    }

    private static EnumStatus readStatus() {
        while (true) {
            System.out.print("Статус (1 - NEW, 2 - IN_PROGRESS, 3 - DONE): ");
            switch (scanner.nextLine().trim()) {
                case "1" -> { return EnumStatus.NEW; }
                case "2" -> { return EnumStatus.IN_PROGRESS; }
                case "3" -> { return EnumStatus.DONE; }
                default -> System.out.println("Введите 1, 2 или 3");
            }
        }
    }
}