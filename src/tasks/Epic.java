package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtask(int id) {
        subtaskIds.add(id);
    }
    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
    public void removeSubtask(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }
}
