package tracker.manager;
import tracker.model.*;
import static tracker.model.EnumStatus.*;
import static tracker.model.TaskType.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    abstract T createManager();   // каждый наследник создаёт свою реализацию

    @BeforeEach
    void init() {
        taskManager = createManager();
    }

    @Test
    void addNewTaskShouldSaveTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW, TASK,
                60, null);
        final int taskId = taskManager.addNewTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getTasksShouldReturnEmptyListWhenNoTasks() {
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "getTasks() не должен возвращать null");
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустым");
     }

   @Test
    void getTaskWithWrongIdShouldReturnNull() {
        assertNull(taskManager.getTask(999),"getTasks() должен возвращать null" );
    }

   @Test
    void addNewSubtaskShouldFailWhenEpicNotExists() {
        TaskSubtask subtask = new TaskSubtask("S", "d", NEW, 999, SUBTASK,
                60, null);

        assertThrows(IllegalArgumentException.class,
                () -> taskManager.addNewSubtask(subtask),
                "Должно быть исключение, если эпика с id=999 нет");
    }

    @Test
    void deleteTaskShouldRemoveTaskAndUpdateState() {

        TaskEpic epic = new TaskEpic("E", "d", DONE, EPIC,
                60, null);
        final int epicId = taskManager.addNewEpic(epic);
        TaskSubtask subtask = new TaskSubtask("S", "d", IN_PROGRESS, epic.getTaskID(), SUBTASK,
                60, null);
        final int subtaskId  = taskManager.addNewSubtask(subtask);

        assertTrue(taskManager.getEpic(epicId).getSubtaskIds().contains(subtaskId),
                "Подзадача должна быть в эпике до удаления");
        assertEquals(IN_PROGRESS, taskManager.getEpic(epicId).getTaskStatus(),
                "Эпик с IN_PROGRESS-подзадачей должен быть IN_PROGRESS");

        taskManager.deleteSubtask(subtaskId);

        assertNull(taskManager.getSubtask(subtaskId), "Задача не удалена");
        assertTrue(taskManager.getEpic(epicId).getSubtaskIds().isEmpty(),
                "Эпик не должен содержать удалённую подзадачу");
        assertEquals(NEW, taskManager.getEpic(epicId).getTaskStatus(),
                "Эпик без подзадач должен быть NEW");
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

    @Test
    void saveAndLoadedEmptyTask() throws IOException {
        File file = File.createTempFile("test", ".csv");
        file.deleteOnExit();

        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.save();

        FileBackedTasksManager loaded = FileBackedTasksManager.loadFromFile(file);

        assertTrue(loaded.getTasks().isEmpty(),"Список задач должен быть пустым");
        assertTrue(loaded.getEpics().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loaded.getSubtasks().isEmpty(), "Список подзадач должен быть пустым");
        assertTrue(loaded.getHistory().isEmpty(), "История должна быть пустой");

    }

    @Test
    void saveAndLoadedEpicWithoutSubtask() throws IOException {
        File file = File.createTempFile("test", ".csv");
        file.deleteOnExit();

        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC,
                60, null);
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.addNewEpic(epic);
        manager.save();

        FileBackedTasksManager loaded = FileBackedTasksManager.loadFromFile(file);

        assertEquals(epic, loaded.getEpics().get(0), "Эпик в менеджере не равен эпику в файле");
        assertTrue(loaded.getTasks().isEmpty(),"Список задач должен быть пустым");
        assertTrue(loaded.getSubtasks().isEmpty(), "Список подзадач должен быть пустым");
        assertTrue(loaded.getHistory().isEmpty(), "История должна быть пустой");
    }


}

