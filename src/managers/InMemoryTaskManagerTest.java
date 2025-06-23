package managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.List;
import java.util.WeakHashMap;


class InMemoryTaskManagerTest {
    private final TaskManager manager = new InMemoryTaskManager() {

        @Override
        public Subtask getSubtasks(int epicId) {
            return (Subtask) List.of();
        }

        @Override
        public void deleteTask(int id) {

        }

        @Override
        public void deleteSubtask(int id) {

        }
    }; // не понимаю, если убрать эту ; то весь код крашится

    @BeforeEach
    void setup() {
    }

    @Test
    public void createAndGetTask() {
        Task task = new Task("Задача", "Описание");
        Task create = manager.updateTask(task);

        Task get = manager.getTask(create.getId());
        assertEquals(create, get);
    }

    @Test
    void updateTask() {
        Task task = new Task("Задача", "Описание");
        Task created = manager.updateTask(task);
        created.getName("Задача обновлена");

        manager.updateTask(created);
        Task updated = manager.getTask(created.getId());
        assertEquals("Задача обновлена", updated.getName());
    }

    @Test
    void updateEpicPreservingSubtasks() {
        Epic epic = manager.updateEpic(new Epic("Эпик", "Описанеие"));
        Subtask sub = manager.updateSubtask(new Subtask("Задача", "Описание", epic.getId()));

        epic.getName("Эпик Обновлен");
        manager.updateEpic(epic);

        Epic updated = manager.getEpic(epic.getId());
        assertTrue(updated.getSubtaskIds().contains(sub.getId()));
        assertEquals("Эпик Обновлен", updated.getName());
    }

    @Test
    void createAndGetSubtask() {
        Epic epic = manager.updateEpic(new Epic("Эпик", "Описанеие"));
        Subtask sub = new Subtask("Задача", "Описание", epic.getId());
        Subtask created = manager.updateSubtask(sub);

        Subtask retrieved;
        retrieved = (Subtask) manager.getSubtasks(created.getId());
        assertEquals(created, retrieved);
    }

    @Test
    public void updateSubtask() {
        Epic epic = manager.updateEpic(new Epic("Эпик", "Описанеие"));
        Subtask sub = manager.updateSubtask(new Subtask("Задача", "Описание", epic.getId()));
        sub.getName("Задача обновлена");

        manager.updateSubtask(sub);
        Subtask updated;
        updated = manager.getSubtasks(sub.getId());
        assertEquals("Задача обновлена", updated.getName());
    }

    @Test
    public void deleteTask() {
        Task task = manager.updateTask(new Task("Задача", "Описание"));
        manager.deleteTask(task.getId());
        assertNull(manager.getTask(task.getId()));
    }

    @Test
    public void deleteEpicAndSubtasks() {
        Epic epic = manager.updateEpic(new Epic("Эпик", "Описанеие"));
        Subtask sub = manager.updateSubtask(new Subtask("", "", epic.getId()));

        manager.getEpic(epic.getId());
        assertNull(manager.getEpic(epic.getId()));
        assertNull(manager.getSubtasks(sub.getId()));
    }

    @Test
    public void deleteSubtaskAndRemoveFromEpic() {
        Epic epic = manager.updateEpic(new Epic("Эпик", "Описанеие"));
        Subtask sub = manager.updateSubtask(new Subtask("Задача", "Описание", epic.getId()));

        manager.deleteSubtask(sub.getId());
        assertNull(manager.getSubtasks(sub.getId()));
        assertFalse(manager.getEpic(epic.getId()).getSubtaskIds().contains(sub.getId()));
    }

    @Test
    public void returnSubtasksOfEpic() {
        Epic epic = manager.updateEpic(new Epic("Эпик", "Описанеие"));
        Subtask sub1 = manager.updateSubtask(new Subtask("Задача №1", "Описание", epic.getId()));
        Subtask sub2 = manager.updateSubtask(new Subtask("Задача №2","Описание", epic.getId()));

        List<Subtask> subtasks = manager.getEpicSubtasks(epic.getId());
        assertTrue(subtasks.contains(sub1));
        assertTrue(subtasks.contains(sub2));
    }

    @Test
    public void shouldHistory() {
        Task task = manager.updateTask(new Task("Задача", "Описание"));
        manager.getTask(task.getId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.getFirst());
    }
}