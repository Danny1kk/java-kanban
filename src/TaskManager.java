import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

interface TaskManager {
    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);

    ArrayList<Task> getAllTasks();
    ArrayList<Epic> getAllEpics();
    ArrayList<Subtask> getAllSubtask();

    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    ArrayList<Subtask> getSubtasksByEpic(int epicId);

    void updateTask(Task task);
    void updateSubtask(Subtask subtask);
    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

}