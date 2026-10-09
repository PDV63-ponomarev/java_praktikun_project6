package methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DeleteTask {
    private Scanner scanner = new Scanner(System.in);
    private CreatTask createTask;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, TaskEpic> epics;
    private HashMap<Integer, TaskSubtask> subtasks;
    private ShowTask showTask;
    private HistoryManager historyManager;

    public  DeleteTask(CreatTask createTask) {
        this.createTask = createTask;
        this.tasks = createTask.getTasks();
        this.epics = createTask.getEpics();
        this.subtasks = createTask.getSubtasks();
        this.showTask = new ShowTask(createTask);
        this.historyManager = createTask.getHistoryManager();
    }

    public void deleteAllTasks() {

        System.out.println("Удалить все задачи? \n" +
                "1 - Да \n" +
                "2 - Нет");
        while (true) {
            try {
                int userInput = scanner.nextInt();
                switch (userInput) {
                    case 1:
                        tasks.clear();
                        epics.clear();
                        subtasks.clear();
                        System.out.println("Все задачи удалены");
                        return;
                    case 2:
                        return;
                    default:
                        System.out.println("Введите 1 или 2");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Ошибка ввода: введите 1 или 2");
                scanner.nextLine();
            }
        }
    }

    public void deleteOneTask(){
        showTask.showAllTasks();
        System.out.println("Выберите ID задачи для удаления:");

        String input = scanner.nextLine().trim();

        try {
            int id = Integer.parseInt(input);

            if (tasks.containsKey(id)) {
                Task task = tasks.remove(id);
                historyManager.remove(id);
                System.out.println("Задача " + task.getTaskName() +" удалена");

            } else if (epics.containsKey(id)) {
                TaskEpic epic = epics.remove(id);
                for (int subtaskId : epic.getSubtaskIds()) {
                    subtasks.remove(subtaskId);
                    historyManager.remove(subtaskId);
                }
                historyManager.remove(id);
                System.out.println("Задача " + epic.getTaskName() +" удалена");
            } else if (subtasks.containsKey(id)) {
                TaskSubtask subtask = subtasks.remove(id);
                TaskEpic epic = epics.get(subtask.getEpicId());
                if (epic != null) {
                    epic.removeSubtaskId(id);
                    epic.updateStatus(new ArrayList<>(subtasks.values()));
                }
                historyManager.remove(id);
                System.out.println("Задача " + subtask.getTaskName() +" удалена");
            } else {
                System.out.println("Задача с ID " + id + " не найдена");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Ошибка: нужно ввести число");
        }
    }
}
