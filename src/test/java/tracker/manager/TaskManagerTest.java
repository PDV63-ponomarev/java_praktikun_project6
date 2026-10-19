package tracker.manager;
import tracker.model.*;
import static tracker.model.EnumStatus.*;
import static tracker.model.TaskType.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    abstract T createManager();   // каждый наследник создаёт свою реализацию

    @BeforeEach
    void init() {
        taskManager = createManager();
    }

//    @Test
    void addNewTaskShouldSaveTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW, TASK);
        final int taskId = taskManager.addNewTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

//    @Test
    void getTasksShouldReturnEmptyListWhenNoTasks() {
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "getTasks() не должен возвращать null");
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустым");
     }

//    @Test
    void getTaskWithWrongIdShouldReturnNull() {
        assertNull(taskManager.getTask(999),"getTasks() должен возвращать null" );
    }

//    @Test
    void addNewSubtaskShouldFailWhenEpicNotExists() {
        TaskSubtask subtask = new TaskSubtask("S", "d", NEW, 999, SUBTASK);

        assertThrows(IllegalArgumentException.class,
                () -> taskManager.addNewSubtask(subtask),
                "Должно быть исключение, если эпика с id=999 нет");
    }

    @Test
    void deleteTaskShouldRemoveTaskAndUpdateState() {

        // ДОДЕЛАТЬ! Проверка подчишения истории и изменение статуса эпика

        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW, TASK);
        final int taskId = taskManager.addNewTask(task);

        assertNotNull(taskManager.getTask(taskId), "Задача не найдена.");

        taskManager.deleteTask(taskId);

        assertNull(taskManager.getTask(taskId), "Задача не удалена");
    }
}

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    FileBackedTasksManager createManager() {
        return new FileBackedTasksManager(new File("test.csv"));
    }
}

