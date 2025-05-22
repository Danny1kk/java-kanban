package tasks;

public class Subtask extends Task {
    private final long epicId;

    public Subtask(String name, String description, TaskStatus status, long epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public  long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + taskStatus +
                "epicId=" + epicId +
                '}';
    }

}
