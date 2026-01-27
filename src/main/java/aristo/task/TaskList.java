package aristo.task;

import java.util.ArrayList;

import aristo.exception.AristoException;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    private void validateIndex(int index) throws AristoException {
        if (index < 1 || index > tasks.size()) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }
    }

    public Task removeTask(int index) throws AristoException {
        validateIndex(index);
        return tasks.remove(index - 1);
    }

    public Task getTask(int index) throws AristoException {
        validateIndex(index);
        return tasks.get(index - 1);
    }

    public ArrayList<Task> asList() {
        return new ArrayList<>(tasks);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
