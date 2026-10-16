package tracker.manager;
import tracker.model.*;
import tracker.manager.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tracker.model.EnumStatus.NEW;
import static tracker.model.TaskType.EPIC;


public class EpicStatusTest {
    @Test
    void epicWithNoSubtasksShouldBeNew() {
        // given — подготовка
        TaskEpic epic = new TaskEpic("E", "d", NEW,  EPIC);

        // when — действие
        epic.updateStatus(List.of());

        // then — проверка
        assertEquals(NEW, epic.getTaskStatus());
    }

    /*
    @Test
    void epicWithAllNewSubtasksShouldBeNew() { ... }
    @Test
    void epicWithAllDoneSubtasksShouldBeDone() { ... }
    @Test
    void epicWithNewAndDoneSubtasksShouldBeInProgress() { ... }
    @Test
    void epicWithInProgressSubtaskShouldBeInProgress() { ... }

     */
}
