package aristo.task;

import java.time.LocalDate;

/**
 * Represents an abstract task in the Aristo task list.
 * <p>
 * A task has a description and a completion status. Subclasses define
 * specific types of tasks and how they are stored.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * The task is initially not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns status icon corresponding to this task's completion status.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns a string representation of the task for display purposes.
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }

    /**
     * Returns whether this task is marked as completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description for this task.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a string representation of this task for file storage.
     */
    public abstract String toFileString();

    /**
     * Returns a boolean indicating if the task occurs on the given date.
     */
    public boolean isOccurringOn(LocalDate date) {
        return false;
    }
}
