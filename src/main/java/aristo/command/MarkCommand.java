package aristo.command;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to mark a task as done.
 */
public class MarkCommand extends Command {
    private final Ui ui;

    public MarkCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        if (argument.isBlank()) {
            throw new AristoException("Please specify a task number to mark!\n");
        }

        int taskIndex = Parser.parseTaskIndex(argument);
        Task task = taskList.getTask(taskIndex);

        if (task.isDone()) {
            throw new AristoException("Task " + taskIndex + " has already been marked as done.\n");
        }

        task.markAsDone();
        storage.saveTasks(taskList);
        return ui.showTaskMarked(task);
    }
}
