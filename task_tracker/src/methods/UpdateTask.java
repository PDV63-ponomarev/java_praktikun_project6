package methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UpdateTask {
    private Scanner scanner = new Scanner(System.in);
    private CreatTask createTask;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, TaskEpic> epics;
    private HashMap<Integer, TaskSubtask> subtasks;
    private ShowTask showTask;

    public  UpdateTask(CreatTask createTask) {
        this.createTask = createTask;
        this.tasks = createTask.getTasks();
        this.epics = createTask.getEpics();
        this.subtasks = createTask.getSubtasks();
        this.showTask = new ShowTask(createTask);
    }

    public void managerUpdateTask(){

        showTask.showAllTasks();

        System.out.println("Введите ID задачи для обновления:");

        int id = checkIDTask();
        if (id == -1) {
            return;
        }

        System.out.println("Что хотите обновить: \n" +
                "1 - Имя \n" +
                "2 - Описание \n" +
                "3 - Статус (Не доступно большим задачам) \n" +
                "4 - Отмена");
        while (true) {
            try {
                int userInput = Integer.parseInt(scanner.nextLine());
                switch (userInput) {
                    case 1:
                        updateName(id);
                        return;
                    case 2:
                        updateDescription(id);
                        return;
                    case 3:
                        if (epics.containsKey(id)) {
                            System.out.println("Нельзя изменить статус большой задачи вручную!");
                            return;
                        }
                        updateStatus(id);
                        return;
                    case 4:
                        System.out.println("Обновление отменено");
                        return;
                }
            } catch (InputMismatchException e) {
                    System.out.println("Ошибка ввода: введите от 1 до 4");
                    scanner.nextLine();
                }
            }
    }

    private int checkIDTask() {
        String input = scanner.nextLine().trim();
        int id = Integer.parseInt(input);
        try {

            if (tasks.containsKey(id)) {
                Task task = tasks.get(id);
                System.out.println("Выбрана задача: " + task.getTaskName());
                return id;

            } else if (epics.containsKey(id)) {
                TaskEpic epic = epics.get(id);
                System.out.println("Выбрана большая задача: " + epic.getTaskName());
                return id;

            } else if (subtasks.containsKey(id)) {
                TaskSubtask subtask = subtasks.get(id);
                System.out.println("Выбрана подзадача: " + subtask.getTaskName());
                return id;

            } else {
                System.out.println("Задача с ID " + id + " не найдена");
                return -1;
            }

        } catch (InputMismatchException e) {
            System.out.println("Ошибка: нужно ввести число");
            return -1;
        }
    }

    private void updateName(int id){

        System.out.println("Введите новое имя:");
        String inputName = scanner.nextLine();

        if (inputName.isEmpty()) {
            System.out.println("Имя не может быть пустым");
            return;
        }

        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            task.setTaskName(inputName);
            System.out.println("Имя задачи обновлено на: " + inputName);
        } else if (epics.containsKey(id)) {
            TaskEpic epic = epics.get(id);
            System.out.println("Имя большой задачи обновлено на: " + inputName);
            epic.setTaskName(inputName);
        } else if (subtasks.containsKey(id)){
            TaskSubtask subtask = subtasks.get(id);
            System.out.println("Имя подзадачи обновлено на: " + inputName);
            subtask.setTaskName(inputName);
        } else {
            System.out.println("Задача с ID " + id + " не найдена");
        }
    }

    private void updateDescription(int id){
        System.out.println("Введите новое описание:");
        String inputDescription = scanner.nextLine();

        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            task.setTaskDescription(inputDescription);
            System.out.println("Описание задачи обновлено");
        } else if (epics.containsKey(id)) {
            TaskEpic epic = epics.get(id);
            epic.setTaskDescription(inputDescription);
            System.out.println("Описание большой задачи обновлено");
        } else if(subtasks.containsKey(id)){
            TaskSubtask subtask = subtasks.get(id);
            subtask.setTaskDescription(inputDescription);
            System.out.println("Описание подзадачи обновлено");
        } else {
            System.out.println("Задача с ID " + id + " не найдена");
        }
    }

    private void updateStatus(int id){
        System.out.println("Введите новое статус: \n" +
                "1 - Новая (NEW) \n" +
                "2 - Выполняется (IN_PROGRESS) \n" +
                "3 - Выполнена (DONE)");

        EnumStatus status = null;
        boolean validInput = false;

        while (!validInput) {
            try {
                int userInput = Integer.parseInt(scanner.nextLine());
                switch (userInput) {
                    case 1:
                        status = EnumStatus.NEW;
                        validInput = true;
                        break;
                    case 2:
                        status = EnumStatus.IN_PROGRESS;
                        validInput = true;
                        break;
                    case 3:
                        status = EnumStatus.DONE;
                        validInput = true;
                        break;
                    default:
                        System.out.println("Неверный выбор. Введите 1, 2 или 3:");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода: выберите 1, 2, 3");
                scanner.nextLine();
            }
        }

        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            task.setTaskStatus(status);
            System.out.println("Статус задачи обновлен на: " + status);
        } else if (subtasks.containsKey(id)) {
            TaskSubtask subtask = subtasks.get(id);
            subtask.setTaskStatus(status);
            
            TaskEpic epic = epics.get(subtask.getEpicId());
            epic.updateStatus(new ArrayList<>(subtasks.values()));
            System.out.println("Статус задачи обновлен на: " + status);
        } else {
            System.out.println("Задача с ID " + id + " не найдена");
        }

    }

}
