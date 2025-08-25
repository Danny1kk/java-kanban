package test;


import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void setup() {
        manager = Managers.getDefault();
    }

    @Test
    void createAndGetTask() {
        Task task = new Task("Задача", "Описание");
        manager.addTask(task);
        Task retrieved = manager.getTask(task.getId());

        assertEquals(task, retrieved);
    }

    @Test
    void updateTask() {
        Task task = new Task("Задача", "Описание");
        manager.addTask(task);
        task.setName("Обновлённая задача");

        manager.updateTask(task);
        Task updated = manager.getTask(task.getId());

        assertEquals("Обновлённая задача", updated.getName());
    }

    @Test
    void createAndGetEpicWithSubtasks() {
        Epic epic = new Epic("Эпик", "Описание");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Сабтаск", "Описание", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        Subtask retrieved = manager.getSubtask(subtask.getId());
        assertEquals(subtask, retrieved);
    }

    @Test
    void updateEpicPreservesSubtasks() {
        Epic epic = new Epic("Эпик", "Описание");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Сабтаск", "Описание", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        epic.setName("Обновлённый эпик");
        manager.updateEpic(epic);

        Epic updatedEpic = manager.getEpic(epic.getId());
        assertEquals("Обновлённый эпик", updatedEpic.getName());
        assertTrue(updatedEpic.getSubtaskIds().contains(subtask.getId()));
    }


    @Test
    void deleteTaskById() {
        Task task = new Task("Задача", "Описание");
        manager.addTask(task);
        manager.deleteAllTasks();

        assertNull(manager.getTask(task.getId()));
    }

    @Test
    void deleteEpicAlsoDeletesSubtasks() {
        Epic epic = new Epic("Эпик", "Описание");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Сабтаск", "Описание", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        manager.removeEpic(epic.getId());

        assertNull(manager.getEpic(epic.getId()));
        assertNull(manager.getSubtask(subtask.getId()));
    }

    @Test
    void deleteSubtaskRemovesItFromEpic() {
        Epic epic = new Epic("Эпик", "Описание");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Сабтаск", "Описание", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        manager.removeSubtask(subtask.getId());

        assertNull(manager.getSubtask(subtask.getId()));
        assertFalse(manager.getEpic(epic.getId()).getSubtaskIds().contains(subtask.getId()));
    }

    @Test
    void shouldReturnHistory() {
        Task task = new Task("Задача", "Описание");
        manager.addTask(task);

        manager.getTask(task.getId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }
}