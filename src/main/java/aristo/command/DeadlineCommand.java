package aristo.command;

import java.time.format.DateTimeParseException;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.Deadline;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to add a new Deadline task to the task list.
 */
public class DeadlineCommand extends Command {
    private final Ui ui;

    public DeadlineCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        if (argument.isBlank()) {
            throw new AristoException("Please provide a task description and its deadline! e.g XXX /by YYY\n");
        }

        String[] taskComponents = Parser.parseDeadline(argument);
        String description = taskComponents[0];
        String deadline = taskComponents[1];

        try {
            Deadline deadlineTask = new Deadline(description, deadline);
            taskList.addTask(deadlineTask);
            storage.saveTasks(taskList);
            return ui.showDeadlineTaskAdded(deadlineTask) + printNumberOfTasks();
        } catch (DateTimeParseException e) {
            throw new AristoException("Invalid date format! Please enter a valid date in the format yyyy-MM-dd.\n");
        }
    }

    /**
     * Displays the number of tasks currently in the task list.
     */
    private String printNumberOfTasks() {
        return ui.printNumberOfTasks(taskList);
    }
}
