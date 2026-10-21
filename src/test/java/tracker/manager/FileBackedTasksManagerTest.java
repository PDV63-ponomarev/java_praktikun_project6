package tracker.manager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tracker.model.*;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tracker.model.EnumStatus.NEW;
import static tracker.model.TaskType.EPIC;
import static tracker.model.TaskType.TASK;

//public class FileBackedTasksManagerTest {
//
//    @Test
//    void savedAndLoadedStateShouldMatch() {
//        File file = new File("tasks.csv");
//        FileBackedTasksManager manager = new FileBackedTasksManager(file);
//
//
//        Task task = new Task("T1", "d", NEW, TASK);
//        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC);
//
//        // создали эпик + подзадачу + обычную задачу, просмотрели пару задач (история)
//        FileBackedTasksManager loaded = FileBackedTasksManager.loadFromFile(file);
//
//        assertEquals(manager.getTasks(), loaded.getTasks());
//        assertEquals(manager.getEpics(), loaded.getEpics());
//        assertEquals(manager.getHistory(), loaded.getHistory());
//    }
//
//}
