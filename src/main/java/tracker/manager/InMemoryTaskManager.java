package tracker.manager;

import tracker.model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, TaskEpic> epics = new HashMap<>();
    protected final HashMap<Integer, TaskSubtask> subtasks = new HashMap<>();
    protected int nextId = 1;
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();
//    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Task::getTaskID)
    );

    protected void addToPrioritized(Task t) { prioritizedTasks.add(t); }

    @Override
    public List<Task> getTasks()      { return new ArrayList<>(tasks.values()); }
    @Override
    public List<TaskEpic> getEpics()  { return new ArrayList<>(epics.values()); }
    @Override
    public List<TaskSubtask> getSubtasks() { return new ArrayList<>(subtasks.values()); }

    private void removeFromPrioritized(int id) {
        prioritizedTasks.removeIf(t -> t.getTaskID() == id);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public TaskEpic getEpic(int id) {
        TaskEpic epic = epics.get(id);
        if (epic != null) historyManager.add(epic);
        return epic;
    }

    @Override
    public TaskSubtask getSubtask(int id) {
        TaskSubtask sub = subtasks.get(id);
        if (sub != null) historyManager.add(sub);
        return sub;
    }

    @Override
    public int addNewTask(Task task) {
        task.setTaskID(nextId++);
        tasks.put(task.getTaskID(), task);

        prioritizedTasks.add(task);

        return task.getTaskID();
    }

    @Override
    public int addNewEpic(TaskEpic epic) {
        epic.setTaskID(nextId++);
        epics.put(epic.getTaskID(), epic);

        prioritizedTasks.add(epic);

        return epic.getTaskID();
    }

    @Override
    public int addNewSubtask(TaskSubtask subtask) {
        TaskEpic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new IllegalArgumentException(
                    "Эпик с id=" + subtask.getEpicId() + " не найден");
//            return -1;
        }
        subtask.setTaskID(nextId++);
        subtasks.put(subtask.getTaskID(), subtask);
        epic.addSubtaskId(subtask.getTaskID());
        updateEpicStatus(epic.getTaskID());

        prioritizedTasks.add(subtask);

        return subtask.getTaskID();
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getTaskID())) {
            removeFromPrioritized(task.getTaskID());
            tasks.put(task.getTaskID(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(TaskEpic epic) {
        if (epics.containsKey(epic.getTaskID())) {
            removeFromPrioritized(epic.getTaskID());
            epics.put(epic.getTaskID(), epic);
            prioritizedTasks.add(epic);
        }
    }

    @Override
    public void updateSubtask(TaskSubtask subtask) {
        if (!subtasks.containsKey(subtask.getTaskID())) return;
        removeFromPrioritized(subtask.getTaskID());
        subtasks.put(subtask.getTaskID(), subtask);
        updateEpicStatus(subtask.getEpicId());
        prioritizedTasks.add(subtask);
    }

    private void updateEpicStatus(int epicId) {
        TaskEpic epic = epics.get(epicId);
        epic.recalculate(new ArrayList<>(subtasks.values()));
        removeFromPrioritized(epic.getTaskID());
        prioritizedTasks.add(epic);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);

        Task t = getTask(id);
        prioritizedTasks.remove(t);

        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        TaskEpic epic = epics.remove(id);
        if (epic == null) return;
        for (int subId : epic.getSubtaskIds()) {
            subtasks.remove(subId);
            historyManager.remove(subId);
        }
        Task t = getTask(id);
        prioritizedTasks.remove(t);
        historyManager.remove(id);
    }


    @Override
    public void deleteSubtask(int id) {
        TaskSubtask sub = subtasks.remove(id);
        if (sub == null) return;
        TaskEpic epic = epics.get(sub.getEpicId());
        if (epic != null) {
            epic.removeSubtaskId(id);
            updateEpicStatus(epic.getTaskID());
        }
        Task t = getTask(id);
        prioritizedTasks.remove(t);
        historyManager.remove(id);
    }

    @Override
    public void deleteTasks() {
        for (int id : tasks.keySet()) historyManager.remove(id);
        prioritizedTasks.removeIf(t -> t.getTaskType() == TaskType.TASK);
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (TaskEpic e : epics.values()) {
            for (int subId : e.getSubtaskIds()) {
                subtasks.remove(subId);
                historyManager.remove(subId);
            }
            historyManager.remove(e.getTaskID());
        }
        prioritizedTasks.removeIf(t -> t.getTaskType() == TaskType.EPIC);
        epics.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (TaskSubtask s : subtasks.values()) {
            TaskEpic epic = epics.get(s.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(s.getTaskID());
                updateEpicStatus(epic.getTaskID());
            }
            historyManager.remove(s.getTaskID());
        }
        prioritizedTasks.removeIf(t -> t.getTaskType() == TaskType.SUBTASK);
        subtasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

}