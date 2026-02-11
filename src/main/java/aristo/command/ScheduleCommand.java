package aristo.command;

import java.time.LocalDate;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Command to view all tasks scheduled for a specific date.
 */
public class ScheduleCommand extends Command {
    private final Ui ui;

    public ScheduleCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        if (argument.isBlank()) {
            throw new AristoException("Please provide a date to view tasks! Format: schedule YYYY-MM-DD\n");
        }

        LocalDate date = Parser.parseDate(argument);
        TaskList tasksOnDate = taskList.getTasksOn(date);
        return ui.printTasksOnDate(tasksOnDate, date);
    }
}