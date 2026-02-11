package aristo.command;

import aristo.exception.AristoException;
import aristo.storage.TaskStorage;
import aristo.task.TaskList;

/**
 * Abstract class to represent a user command.
 */
public abstract class Command {
    protected TaskList taskList;
    protected TaskStorage storage;

    /**
     * Constructor for Command.
     *
     * @param taskList The task list to operate on.
     * @param storage  The storage for persisting tasks.
     */
    public Command(TaskList taskList, TaskStorage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

    /**
     * Executes the command with the given argument.
     *
     * @param argument The argument passed to the command.
     * @return A response message indicating the result of the command execution.
     * @throws AristoException If an error occurs during command execution.
     */
    public abstract String execute(String argument) throws AristoException;
}
