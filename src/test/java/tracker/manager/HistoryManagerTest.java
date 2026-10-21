package tracker.manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tracker.model.EnumStatus.NEW;
import static tracker.model.TaskType.TASK;


public class HistoryManagerTest{

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager(); // ← конкретная реализация
        task1 = new Task("T1", "d", NEW, TASK);
        task2 = new Task("T2", "d", NEW, TASK);
        task3 = new Task("T3", "d", NEW, TASK);
        task1.setTaskID(1);   // id нужны, иначе дубли не отличить
        task2.setTaskID(2);
        task3.setTaskID(3);
    }

    @Test
    void historyShouldBeEmpty(){
        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "История должна быть пустой");
    }

    @Test
    void addShouldNotDuplicateAndMoveToEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);              // дубль

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "Дубль не должен увеличивать историю");
        assertEquals(task2, history.get(0), "Старый просмотр должен уйти вперёд");
        assertEquals(task1, history.get(1), "Повторный просмотр — в конце");
    }

    @Test
    void deleteTaskForHistory(){
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        history.remove(0);

        assertTrue(history.isEmpty(), "История должна быть пустой");

        historyManager.add(task1);
        historyManager.add(task2);

        history = historyManager.getHistory();
        history.remove(0);

        assertEquals(task2, history.get(0), "Вторая задача должна встать на место первой");

        historyManager.add(task1);
        historyManager.add(task3);

        history = historyManager.getHistory();
        history.remove(1);

        assertEquals(task3, history.get(1), "На место второй задачи должна встать третья");
    }
}

