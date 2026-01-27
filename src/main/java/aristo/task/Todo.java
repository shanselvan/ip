package aristo.task;

/**
 * Represents a <code>Todo</code> task in the Aristo task list.
 * <p>
 * A <code>Todo</code> task only contains a description.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toFileString() {
        int isDone = this.isDone ? 1 : 0;
        return "T | " + isDone + " | " + this.description;
    }
}
