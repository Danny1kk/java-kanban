import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.*;

public class Main {

    public static void main(String[] args) {
       TaskManager manager = new InMemoryTaskManager();

        System.out.println("Задачи: ");
        Task task1 = new Task("Убраться", "На кухне");
        Task task2 = new Task("Убраться", "В комнате");
        manager.addTask(task1);
        manager.addTask(task2);

        System.out.println("Эпики: ");
        Epic epic1 = new Epic("Подготовка к отпуску", "Собрать всё необходимое");
        Epic epic2 = new Epic("Подготовка к отпуску", "Купитьт корм для животных");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask sub1 = new Subtask("Купить билеты", "На самолёт туда и обратно", epic1.getId());
        Subtask sub2 = new Subtask("Собрать вещи", "Одежда, техника", epic1.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);
        System.out.println();

        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(sub1.getId());
        manager.getSubtask(sub2.getId());
        manager.getTask(task2.getId());
        manager.getTask(task1.getId());

        System.out.println();
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