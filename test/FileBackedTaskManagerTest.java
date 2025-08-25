import manager.FileBackedTaskManager;
import tasks.Task;

import java.io.File;

public class FileBackedTaskManagerTest {
    public static void main(String[] args) throws Exception {
        File tempFile = File.createTempFile("tasks", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task = new Task("Тестовая задача", "Описание");
        manager.addTask(task);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        System.out.println("Загруженные задачи: " + loaded.getAllTasks());

        tempFile.deleteOnExit();
    }
}