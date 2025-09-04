package manager;

import tasks.*;

import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManager implements TaskManager {
    protected int nextId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = new InMemoryHistoryManager();

    private final Set<Task> prioritiTasks = new TreeSet<>(Comparator
            .comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Task::getId));

    public Set<Task> getPrioritiTasks() {
        return prioritiTasks;
    }

    private int generateId() {
        return nextId++;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            prioritiTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Subtask subtask : subtasks.values()) {
            prioritiTasks.remove(subtask);
            historyManager.remove(subtask.getId());
        }
        for (Epic epic : epics.values()) {
            prioritiTasks.remove(epic);
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            prioritiTasks.remove(subtask);
            historyManager.remove(subtask.getId());
        }
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritiTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) return;

        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        prioritiTasks.add(subtask);
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            prioritiTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            prioritiTasks.add(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask existing = subtasks.get(subtask.getId());
        if (existing == null) return;

        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) return;

        prioritiTasks.remove(existing);
        subtasks.put(subtask.getId(), subtask);
        prioritiTasks.add(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existing = epics.get(epic.getId());
            epic.setSubtaskIds(existing.getSubtaskIds());
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void removeTask(int id) {
        Task removed = tasks.remove(id);
        if (removed != null) prioritiTasks.remove(removed);
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : new ArrayList<>(epic.getSubtaskIds())) {
                Subtask removedSub = subtasks.remove(subtaskId);
                if (removedSub != null) {
                    prioritiTasks.remove(removedSub);
                    historyManager.remove(subtaskId);
                }
            }
            prioritiTasks.remove(epic);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask removed = subtasks.remove(id);
        if (removed != null) {
            prioritiTasks.remove(removed);
            historyManager.remove(id);

            Epic epic = epics.get(removed.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return Collections.emptyList();
        }

        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;
        Duration total = Duration.ZERO;
        LocalDateTime minStart = null;
        LocalDateTime maxEnd = null;

        for (Integer id : subtaskIds) {
            Subtask subtask = subtasks.get(id);
            if (subtask == null) {
                continue;
            }
            if (subtask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
            if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                LocalDateTime start = subtask.getStartTime();
                LocalDateTime end = subtask.getEndTime();

                if (minStart == null || start.isBefore(minStart)) minStart = start;
                if (maxEnd == null || (end != null && end.isAfter(maxEnd))) maxEnd = end;

                total = total.plus(subtask.getDuration());
            }
        }

        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }

        epic.setStartTime(minStart);
        epic.setEndTime(maxEnd);
        epic.setDuration(total);
    }
}