package tasks;

public class Subtask extends Task {
    private final long epicId;

    public Subtask(Long id, String name, String description, TaskStatus status, long epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public  long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                '}';
    }
}
