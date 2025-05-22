package tasks;

import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public void createSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void deleteSubtasks() {
        subtasks.clear();
    }


    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                "subtasks=" + subtasks +
                '}';
    }
}
