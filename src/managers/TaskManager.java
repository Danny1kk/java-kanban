package managers;

import java.util.List;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public interface TaskManager {
    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);
    Task updateTask(Task task);
    Epic updateEpic(Epic epic);
    Subtask updateSubtask(Subtask subtask);

    List<Subtask> getSubtasks();

    Task getTask(int id);
    Epic getEpic(int id);

    void getSubtask(int id);

    default List<Task> getTasks() {
        return null;
    }

    default List<Epic> getEpics() {
        return null;
    }

    default Subtask getSubtasks(int epicId) {
        return null;
    }

    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();

    void deleteTask(int id);

    void deleteSubtask(int id);
}

