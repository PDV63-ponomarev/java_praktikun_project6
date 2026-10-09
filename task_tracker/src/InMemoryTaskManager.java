import methods.*;

import java.util.List;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {

    protected CreatTask creatTask;
    private ShowTask showTask;
    private DeleteTask deleteTask;
    private UpdateTask updateTask;
    private Scanner scanner;
    private HistoryManager historyManager;
    private boolean isRunning;

    public InMemoryTaskManager() {
        this.creatTask = new CreatTask();
        this.showTask = new ShowTask(creatTask);
        this.deleteTask = new DeleteTask(creatTask);
        this.updateTask = new UpdateTask(creatTask);
        this.historyManager = creatTask.getHistoryManager();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    @Override
    public void showAllTasks() {
        showTask.showAllTasks();
    }

    @Override
    public void deleteAllTasks() {
        deleteTask.deleteAllTasks();
    }

    @Override
    public void showTaskFromID() {
        showTask.showTaskFromID();
    }

    @Override
    public void creatingNewTask() {
        creatTask.creatingNewTask();
    }

    @Override
    public void managerUpdateTask() {
        updateTask.managerUpdateTask();
    }

    @Override
    public void deleteOneTask() {
        deleteTask.deleteOneTask();
    }

    @Override
    public void showHistory() {
        List<Task> history = historyManager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История просмотров пуста");
            return;
        }

        System.out.println("История просмотров:");
        int counter = 1;
        for (Task task : history) {
            System.out.println(counter++ + ") " + task);
        }
    }

    @Override
    public void exit() {
        System.out.println("Выход");
        isRunning = false;
    }

    public void start() {
        while (isRunning) {
            printMenu();
            handleUserInput();
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("Введите команду:\n" +
                "1 - Получить список задач\n" +
                "2 - Удалить все задачи\n" +
                "3 - Открыть задачу\n" +
                "4 - Создать новую задачу\n" +
                "5 - Редактировать задачу\n" +
                "6 - Удалить задачу\n" +
                "7 - Отобразить историю просмотра задач\n" +
                "8 - Выход");
    }

    private void handleUserInput() {
        try {
            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1:
                    showAllTasks();
                    break;
                case 2:
                    deleteAllTasks();
                    break;
                case 3:
                    showTaskFromID();
                    break;
                case 4:
                    creatingNewTask();
                    break;
                case 5:
                    managerUpdateTask();
                    break;
                case 6:
                    deleteOneTask();
                    break;
                case 7:
                    showHistory();
                    break;
                case 8:
                    exit();
                    break;
                default:
                    System.out.println("Ошибка: введите число от 1 до 8");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Ошибка: введите число от 1 до 8");
            scanner.nextLine();
        }
    }

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.start();
    }
}