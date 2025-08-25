import manager.FileBackedTaskManager;
import manager.TaskManager;
import tasks.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("tasks.csv");
        TaskManager manager = new FileBackedTaskManager(file);

        System.out.println("Задачи: ");
        Task task1 = new Task("Убраться на кухне", "Убраться");
        Task task2 = new Task("Убраться в комнате", "Убраться");
        manager.addTask(task1);
        manager.addTask(task2);

        System.out.println("Эпики: ");
        Epic epic1 = new Epic("Собрать всё необходимое", "Подготовка к отпуску");
        Epic epic2 = new Epic("Купитьт корм для животных", "Подготовка к отпуску");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask sub1 = new Subtask("Купить билеты", "На самолёт туда и обратно", epic1.getId());
        Subtask sub2 = new Subtask("Собрать вещи", "Одежда", epic2.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);
        System.out.println();

        System.out.println("После первого запуска: ");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        System.out.println();
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(file);
        System.out.println("\nПосле загрузки из файла: ");
        System.out.println(loaded.getAllTasks());
        System.out.println(loaded.getAllEpics());
        System.out.println(loaded.getAllSubtasks());

        System.out.println("Эпик, после добавления подзадачи: ");
        System.out.println(manager.getEpic(epic1.getId()));

        sub1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(sub1);
        System.out.println();

        System.out.println();
        System.out.println("Эпик, после выполнения одной подзадачи и перемещение ее в DONE: ");
        System.out.println(manager.getEpic(epic2.getId()));

        sub2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(sub2);
        System.out.println();

        System.out.println();
        System.out.println("Эпик, после выполнения всех подзадач: ");
        System.out.println(manager.getEpic(epic1.getId()));
        System.out.println("История: ");
        for (Task task : manager.getHistory()) {
            System.out.println(task.getName() + " (id = " + task.getId() + ")");
        }

        manager.removeTask(task1.getId());
        manager.removeEpic(epic1.getId());

        System.out.println("\nИстория после удаления: ");
        for (Task task : manager.getHistory()) {
            System.out.println(task.getName() + " (id = " + task.getId() + ")");
        }
        System.out.println();
    }
}