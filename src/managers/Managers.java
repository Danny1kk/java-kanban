package managers;

import tasks.Subtask;

import java.util.List;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager() {
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
        };
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
