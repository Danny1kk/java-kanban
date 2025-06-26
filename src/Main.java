import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
       TaskManager manager = new TaskImplimentation();

        System.out.println("Эпик с двумя подзадачами: ");
        Epic epic = new Epic("Подготовка к отпуску", "Собрать всё необходимое");
        manager.addEpic(epic);
        Subtask sub1 = new Subtask("Купить билеты", "На самолёт туда и обратно", epic.getId());
        Subtask sub2 = new Subtask("Собрать вещи", "Одежда, техника", epic.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);
        System.out.println();

        System.out.println();
        System.out.println("Эпик, после добавления подзадачи: ");
        System.out.println(manager.getEpic(epic.getId()));

        sub1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(sub1);
        System.out.println();

        System.out.println();
        System.out.println("Эпик, после выполнения одной подзадачи и перемещение ее в DONE: ");
        System.out.println(manager.getEpic(epic.getId()));

        sub2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(sub2);
        System.out.println();

        System.out.println();
        System.out.println("Эпик, после выполнения всех подзадач: ");
        System.out.println(manager.getEpic(epic.getId()));
        System.out.println();
    }
}
