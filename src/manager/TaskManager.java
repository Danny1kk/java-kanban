package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);

    void updateTask(Task task);
    void updateSubtask(Subtask subtask);
    void updateEpic(Epic epic);

    List<Subtask> getEpicSubtasks(int id);

    List<Task> getHistory();

    void removeTask(int id);
    void removeEpic(int id);
    void removeSubtask(int id);
}
