import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

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

        Subtask sub1 = new Subtask("Купить билеты", "На самолёт туда и обратно", TaskStatus.NEW, epic1.getId());
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(Duration.ofHours(2));
        manager.addSubtask(sub1);

        Subtask sub2 = new Subtask("Собрать вещи", "Одежда", TaskStatus.NEW, epic2.getId());
        sub2.setStartTime(LocalDateTime.now().plusHours(3));
        sub2.setDuration(Duration.ofHours(1));
        manager.addSubtask(sub2);
        System.out.println();

        System.out.println("После первого запуска: ");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        System.out.println();
        System.out.println("\nПосле загрузки из файла: ");

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

        System.out.println("Старт задачи: " + epic1.getStartTime());
        System.out.println("Конец задачи: " + epic1.getEndTime());
        System.out.println("Продолжительность задачи: " + epic1.getDuration());
    }
}