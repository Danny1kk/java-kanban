package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void testAddAndRemove() {
        HistoryManager history = new InMemoryHistoryManager();
        Task task1 = new Task("Тест1", "Описание");
        task1.setId(1);
        Task task2 = new Task("Тест2", "Описание");
        task2.setId(2);

        history.add(task1);
        history.add(task2);
        history.add(task1);

        List<Task> historyList = history.getHistory();
        assertEquals(2, historyList.size());
        assertEquals(task2, historyList.get(0));
        assertEquals(task1, historyList.get(1));

        history.remove(1);
        assertEquals(1, history.getHistory().size());
        assertEquals(task2, history.getHistory().get(0));
    }
}