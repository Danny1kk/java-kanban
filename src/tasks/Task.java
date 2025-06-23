package tasks;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + ": " + name + " [" + status + "]";
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
    }

    public void getName(String updatedTask) {
    }
}