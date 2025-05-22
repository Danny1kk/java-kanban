import java.util.ArrayList;
import java.util.HashMap;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class TaskManager {
    private long taskId = 1;

    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, Subtask> subtasks = new HashMap<>();

    private long getNextId() {
        return taskId++;
    }

    public Task getTask(long id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public boolean createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return true;
    }

    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public void deleteTask(long id) {
        tasks.remove(id);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public boolean createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return true;
    }

    public Epic getEpic(long id) {
        return epics.get(id);
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public boolean updateEpic(Epic epic) {
        Epic existing = epics.get(epic.getId());
        if (existing == null) return false;

        existing.setName(epic.getName());
        existing.setDescription(epic.getDescription());
        updateEpicStatus(existing);
        return true;
    }

    public void deleteEpic(long id) {
        Epic removed = epics.remove(id);
        if (removed != null) {
            for (Subtask subtask : removed.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    public boolean createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        return true;
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    public Subtask getSubtask(long id) {
        return subtasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        Subtask existing = subtasks.get(subtask.getId());
        if (existing == null) return;

        if (existing.getEpicId() != subtask.getEpicId()) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) return;

        epic.deleteSubtask(existing);
        epic.createSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);

        updateEpicStatus(epic);
    }

    public Subtask deleteSubtask(long id) {
        Subtask removed = subtasks.remove(id);
        if (removed != null) {
            Epic epic = epics.get(removed.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(removed);
                updateEpicStatus(epic);
            }
        }
        return removed;
    }

    public void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getTaskStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getTaskStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else if (allNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
