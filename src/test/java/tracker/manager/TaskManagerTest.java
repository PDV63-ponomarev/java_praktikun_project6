package tracker.manager;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
//
//abstract class TaskManagerTest<T extends TaskManager> {
//    protected T taskManager;
//
//    abstract T createManager();   // каждый наследник создаёт свою реализацию
//
//    @BeforeEach
//    void init() {
//        taskManager = createManager();
//    }
//
//    @Test
//    void addNewTaskShouldSaveTask() { /* стандартный кейс из README */ }
//
//    @Test
//    void getTasksShouldReturnEmptyListWhenNoTasks() { /* пустой список */ }
//
//    @Test
//    void getTaskWithWrongIdShouldReturnNull() { assertNull(taskManager.getTask(999)); }
//
//    @Test
//    void addNewSubtaskShouldFailWhenEpicNotExists() { /* несуществующий эпик */ }
//
//    @Test
//    void deleteTaskShouldRemoveTaskAndUpdateState() { ... }
//}
//
//class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
//    @Override InMemoryTaskManager createManager() { return new InMemoryTaskManager(); }
//}
//
//class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
//    @Override FileBackedTasksManager createManager() { return new FileBackedTasksManager(tempFile); }
//}
//
