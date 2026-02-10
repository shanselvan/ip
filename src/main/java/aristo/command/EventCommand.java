package aristo.command;

import java.time.format.DateTimeParseException;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.Event;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to add a new Event task to the task list.
 */
public class EventCommand extends Command {
    private final Ui ui;

    public EventCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        String[] taskComponents = Parser.parseEvent(argument);
        String description = taskComponents[0];
        String from = taskComponents[1];
        String to = taskComponents[2];

        if (from.isBlank()) {
            throw new AristoException("Please specify a from time!\n");
        } else if (to.isBlank()) {
            throw new AristoException("Please specify a to time!\n");
        }

        try {
            Event eventTask = new Event(description, from, to);
            taskList.addTask(eventTask);
            storage.saveTasks(taskList);
            return ui.showEventTaskAdded(eventTask) + printNumberOfTasks();
        } catch (DateTimeParseException e) {
            throw new AristoException("Please use yyyy-MM-dd format for dates!\n");
        }
    }

    /**
     * Displays the number of tasks currently in the task list.
     */
    private String printNumberOfTasks() {
        return ui.printNumberOfTasks(taskList);
    }
}
