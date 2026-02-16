package aristo.command;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to mark a task as not done.
 */
public class UnmarkCommand extends Command {
    private final Ui ui;

    public UnmarkCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        if (argument.isBlank()) {
            throw new AristoException("Please specify a task number to unmark!\n");
        }

        int taskIndex = Parser.parseTaskIndex(argument);
        Task task = taskList.getTask(taskIndex);

        if (!task.isDone()) {
            throw new AristoException("Task " + taskIndex + " has already been marked as not done.\n");
        }

        task.markAsNotDone();
        storage.saveTasksToFile(taskList);
        return ui.showTaskUnmarked(task);
    }
}
