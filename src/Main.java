import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Эпик с двумя подзадачами: ");
        Epic epic = new Epic("Подготовка к отпуску", "Собрать всё необходимое");
        manager.addEpic(epic);
        Subtask sub1 = new Subtask("Купить билеты", "На самолёт туда и обратно", epic.getId());
        Subtask sub2 = new Subtask("Собрать вещи", "Одежда, техника", epic.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);
        Task task = new Task("Выбрать авиалинию по отзывам", "Договориться о встрече");
        manager.addTask(task);

        manager.getTask(task.getId());
        manager.getEpic(epic.getId());
        manager.getSubtask(sub1.getId());

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
            for (Subtask sub : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("·" + sub);
            }
        }
        System.out.println("Подзадачи:");
        for (Subtask sub : manager.getSubtasks()) {
            System.out.println(sub);
        }
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
