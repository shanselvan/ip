package aristo.command;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    private final Ui ui;

    public DeleteCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        if (argument.isBlank()) {
            throw new AristoException("Please specify a task number to delete!\n");
        }

        int taskIndex = Parser.parseTaskIndex(argument);
        Task task = taskList.removeTask(taskIndex);
        storage.saveTasksToFile(taskList);
        return ui.showTaskDeleted(task) + printNumberOfTasks();

    }

    /**
     * Displays the number of tasks currently in the task list.
     */
    private String printNumberOfTasks() {
        return ui.printNumberOfTasks(taskList);
    }
}
