package aristo.task;

import aristo.exception.AristoException;

import java.util.ArrayList;

/**
 * Represents a list of tasks in the Aristo chatbot.
 * <p>
 * This class provides methods to add, remove, retrieve, and query tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs a TaskList containing the given tasks.
     *
     * @param tasks the list of tasks to initialize the TaskList with
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in this task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a given task to this task list.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index 1-based index of the task to remove.
     * @return The task that was removed.
     * @throws AristoException If the given index is invalid.
     */
    public Task removeTask(int index) throws AristoException {
        if (index > tasks.size() || index < 1) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }
        return tasks.remove(index - 1);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index 1-based index of the task to remove.
     * @return The task at the given index.
     * @throws AristoException If the given index is invalid.
     */
    public Task getTask(int index) throws AristoException {
        if (index > tasks.size() || index < 1) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }
        return tasks.get(index - 1);
    }

    /**
     * Returns a copy of this task list.
    */
    public ArrayList<Task> asList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Returns whether this task list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
