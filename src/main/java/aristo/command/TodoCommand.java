package aristo.command;

import aristo.exception.AristoException;
import aristo.storage.TaskStorage;
import aristo.task.TaskList;
import aristo.task.Todo;
import aristo.ui.Ui;

/**
 * Command to add a new Todo task to the task list.
 */
public class TodoCommand extends Command {
    private final Ui ui;

    public TodoCommand(TaskList taskList, TaskStorage storage, Ui ui) {
        super(taskList, storage);
        this.ui = ui;
    }

    @Override
    public String execute(String argument) throws AristoException {
        if (argument.isBlank()) {
            throw new AristoException("Task description is empty, please retry with a valid task description.\n");
        }

        Todo todoTask = new Todo(argument);
        taskList.addTask(todoTask);
        storage.saveTasks(taskList);
        return ui.showTodoTaskAdded(todoTask) + printNumberOfTasks();
    }

    /**
     * Displays the number of tasks currently in the task list.
     */
    private String printNumberOfTasks() {
        return ui.printNumberOfTasks(taskList);
    }
}
