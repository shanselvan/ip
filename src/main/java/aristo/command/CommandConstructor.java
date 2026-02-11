package aristo.command;

import aristo.exception.AristoException;
import aristo.storage.TaskStorage;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Factory class for creating Command instances based on command name.
 */
public class CommandConstructor {
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_SCHEDULE = "schedule";

    private final TaskList taskList;
    private final TaskStorage storage;
    private final Ui ui;

    /**
     * Constructor for CommandConstructor.
     *
     * @param taskList The task list to operate on.
     * @param storage  The storage for persisting tasks.
     * @param ui       The UI object for displaying messages.
     */
    public CommandConstructor(TaskList taskList, TaskStorage storage, Ui ui) {
        this.taskList = taskList;
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Creates and returns a Command based on the command name.
     *
     * @param commandName The name of the command to create.
     * @return The corresponding Command instance.
     * @throws AristoException If the command name is unknown.
     */
    public Command createCommand(String commandName) throws AristoException {
        return switch (commandName) {
            case COMMAND_LIST -> new ListCommand(taskList, storage, ui);
            case COMMAND_MARK -> new MarkCommand(taskList, storage, ui);
            case COMMAND_UNMARK -> new UnmarkCommand(taskList, storage, ui);
            case COMMAND_DELETE -> new DeleteCommand(taskList, storage, ui);
            case COMMAND_TODO -> new TodoCommand(taskList, storage, ui);
            case COMMAND_DEADLINE -> new DeadlineCommand(taskList, storage, ui);
            case COMMAND_EVENT -> new EventCommand(taskList, storage, ui);
            case COMMAND_FIND -> new FindCommand(taskList, storage, ui);
            case COMMAND_SCHEDULE -> new ScheduleCommand(taskList, storage, ui);
            default -> throw new AristoException("Ehm, never heard of that command before!\n");
        };
    }
}
