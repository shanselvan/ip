package aristo.command;

import aristo.exception.AristoException;
import aristo.storage.TaskStorage;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to display all tasks in the task list.
 */
public class ListCommand extends Command {
    private final Ui ui;

    public ListCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        return ui.printTaskList(taskList);
    }
}
