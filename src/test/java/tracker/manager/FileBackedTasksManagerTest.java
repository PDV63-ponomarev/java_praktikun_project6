package tracker.manager;
/*
public class FileBackedTasksManagerTest {

    @TempDir                        // JUnit 5 создаёт временную папку, удалит после теста
    Path tempDir;

    @Test
    void savedAndLoadedStateShouldMatch() {
        Path file = tempDir.resolve("tasks.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        // создали эпик + подзадачу + обычную задачу, просмотрели пару задач (история)
        FileBackedTasksManager loaded = FileBackedTasksManager.loadFromFile(file);
        assertEquals(manager.getTasks(), loaded.getTasks());
        assertEquals(manager.getEpics(), loaded.getEpics());
        assertEquals(manager.getHistory(), loaded.getHistory());
    }

}
 */