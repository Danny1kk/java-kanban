import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Создание задач:");
        Task task1 = new Task("Убрать в квартире", "Начать с кухни", TaskStatus.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Встреча с друзьями", "Позвонить и уточнить на счет встречи", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task2);
        System.out.println();

        System.out.println("Эпик с одной подзадачей:");
        Epic epic1 = new Epic("Пойти в кино", "Посмотреть новый фильм вечером");
        Subtask subtask1 = new Subtask("Выбрать день недели", "Собраться с друзьями в кино", TaskStatus.NEW, epic1.getId());
        System.out.println();

        System.out.println("Эпик с двумя подзадачами:");
        Epic epic2 = new Epic("Подготовка к отпуску", "Собрать всё необходимое");
        Subtask subtask2 = new Subtask("Купить билеты", "На самолёт туда и обратно", TaskStatus.NEW, epic2.getId());
        Subtask subtask3 = new Subtask("Собрать вещи", "Одежда, техника", TaskStatus.IN_PROGRESS, epic2.getId());
        System.out.println();

        System.out.println();
        System.out.println("Вывод всех задач:");
        System.out.println(taskManager.getTask(task1.getId()));
        System.out.println(taskManager.getTask(task2.getId()));
        System.out.println(taskManager.getEpic(epic1.getId()));
        System.out.println(taskManager.getEpic(epic2.getId()));
        System.out.println(taskManager.getSubtask(subtask1.getId()));
        System.out.println(taskManager.getSubtask(subtask2.getId()));
        System.out.println(taskManager.getSubtask(subtask3.getId()));

        System.out.println("Обновление статуса подзадачи и проверка статуса эпика:");
        subtask1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);

        subtask3.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask3);

        System.out.println("Удаление задачи и эпика:");
        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic1.getId());

    }
}
