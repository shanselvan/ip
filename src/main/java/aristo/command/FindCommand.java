package aristo.command;

import aristo.exception.AristoException;
import aristo.storage.TaskStorage;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to find tasks matching a keyword.
 */
public class FindCommand extends Command {
    private final Ui ui;

    public FindCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        if (argument.isBlank()) {
            throw new AristoException("Please provide a keyword to search for!\n");
        }

        TaskList matchingTasks = taskList.find(argument);
        return ui.printMatchingTasks(matchingTasks);
    }
}
