package tracker.manager;
import tracker.model.*;
import tracker.manager.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tracker.model.EnumStatus.*;
import static tracker.model.TaskType.EPIC;
import static tracker.model.TaskType.SUBTASK;


public class EpicStatusTest {
    @Test
    void epicWithNoSubtasksShouldBeNew() {
        // given — подготовка
        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC);

        // when — действие
        epic.updateStatus(List.of());

        // then — проверка
        assertEquals(NEW, epic.getTaskStatus(), "Статус эпика должен быть New");
    }


    @Test
    void epicWithAllNewSubtasksShouldBeNew() {
        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC);
        TaskSubtask subtask = new TaskSubtask("S", "d", NEW, epic.getTaskID(), SUBTASK);


        epic.addSubtaskId(subtask.getTaskID());
        epic.updateStatus(List.of(subtask));

        assertEquals(NEW, epic.getTaskStatus(), "Статус эпика должен быть New");
    }


    @Test
    void epicWithAllDoneSubtasksShouldBeDone() {
        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC);
        TaskSubtask subtask1 = new TaskSubtask("S1", "d", DONE, epic.getTaskID(), SUBTASK);
        TaskSubtask subtask2 = new TaskSubtask("S2", "d", DONE, epic.getTaskID(), SUBTASK);

        // 1. Явно назначаем уникальные id (или добавляем через менеджер)
        subtask1.setTaskID(1);
        subtask2.setTaskID(2);
        // 2. Регистрируем их в эпике
        epic.addSubtaskId(subtask1.getTaskID());
        epic.addSubtaskId(subtask2.getTaskID());
        // 3. Обновляем статус
        epic.updateStatus(List.of(subtask1, subtask2));

        assertEquals(DONE, epic.getTaskStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    void epicWithNewAndDoneSubtasksShouldBeInProgress() {
        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC);
        TaskSubtask subtask1 = new TaskSubtask("S1", "d", NEW, epic.getTaskID(), SUBTASK);
        TaskSubtask subtask2 = new TaskSubtask("S2", "d", DONE, epic.getTaskID(), SUBTASK);

        subtask1.setTaskID(1);
        subtask2.setTaskID(2);
        epic.addSubtaskId(subtask1.getTaskID());
        epic.addSubtaskId(subtask2.getTaskID());
        epic.updateStatus(List.of(subtask1, subtask2));

        assertEquals(IN_PROGRESS, epic.getTaskStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    void epicWithInProgressSubtaskShouldBeInProgress() {
        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC);
        TaskSubtask subtask1 = new TaskSubtask("S1", "d", IN_PROGRESS, epic.getTaskID(), SUBTASK);
        TaskSubtask subtask2 = new TaskSubtask("S2", "d", IN_PROGRESS, epic.getTaskID(), SUBTASK);

        subtask1.setTaskID(1);
        subtask2.setTaskID(2);
        epic.addSubtaskId(subtask1.getTaskID());
        epic.addSubtaskId(subtask2.getTaskID());
        epic.updateStatus(List.of(subtask1, subtask2));

        assertEquals(IN_PROGRESS, epic.getTaskStatus(), "Статус эпика должен быть IN_PROGRESS");
    }
}
