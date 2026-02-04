package aristo;

import java.time.format.DateTimeParseException;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.Deadline;
import aristo.task.Event;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.task.Todo;
import aristo.ui.Ui;

/**
 * Represents the main chatbot application, Aristo, which manages the task list.
 */
public class Aristo {
    private static TaskList taskList;
    private static final Ui ui = new Ui();
    private static final TaskStorage storage = new TaskStorage("./data/aristo.txt");

    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_FIND = "find";


    /**
     * The main entry point of the Aristo chatbot application.
     * Initializes storage and task list, greets the user, and enters the command loop.
     * Processes commands until the user types "bye", after which the application exits.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Aristo aristo = new Aristo();
        ui.greet();
        String userInput = ui.fetchNextCommand();

        while (!userInput.equals(COMMAND_BYE)) {
            aristo.getResponse(userInput);
            userInput = ui.fetchNextCommand();
        }

        ui.exit();
    }

    public String getResponse(String input) {
        taskList = new TaskList(storage.loadTasks());

        try {
            String[] parsed = Parser.parseCommand(input);
            String command = parsed[0];
            String taskIndexString = parsed[1];

            switch (command) {
            case COMMAND_LIST:
                return ui.printTaskList(taskList);

            case COMMAND_MARK:
                int taskIndexInteger = Parser.parseTaskIndex(taskIndexString);
                return handleMarkTask(taskIndexInteger);

            case COMMAND_UNMARK:
                if (taskIndexString.isBlank()) {
                    throw new AristoException("Please specify a task number to unmark!\n");
                }

                int taskIndex = Integer.parseInt(taskIndexString);
                return handleUnmarkTask(taskIndex);

            case COMMAND_DELETE:
                if (taskIndexString.isBlank()) {
                    throw new AristoException("Please specify a task number to delete!\n");
                }

                int taskIndexInt = Integer.parseInt(taskIndexString);
                return handleDeleteTask(taskIndexInt);

            case COMMAND_TODO:
                return handleTodo(taskIndexString);

            case COMMAND_DEADLINE:
                return handleDeadline(taskIndexString);

            case COMMAND_EVENT:
                return handleEvent(taskIndexString);

            case COMMAND_FIND:
                if (taskIndexString.isBlank()) {
                    throw new AristoException("Please provide a keyword to search for!\n");
                }

                TaskList matchingTasks = taskList.find(taskIndexString);
                return ui.printMatchingTasks(matchingTasks);

            default:
                throw new AristoException("Ehm, never heard of that command before!\n");
            }

        } catch (AristoException e) {
            return ui.showError(e.getMessage());
        }
    }

    /**
     * Displays the number of tasks currently in the task list.
     */
    public String printNumberOfTasks() {
        return ui.printNumberOfTasks(taskList);
    }


    /**
     * Marks the specified task as done.
     *
     * @param taskIndexInteger 1-based index of the task to mark.
     * @return The message string indicating if the task has been marked.
     * @throws AristoException If the task is already marked as done or index is invalid.
     */
    public String handleMarkTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.getTask(taskIndexInteger);

        if (task.isDone()) {
            throw new AristoException("Task " + taskIndexInteger + " has already been marked as done.\n");
        }

        task.markAsDone();
        storage.saveTasks(taskList);
        return ui.showTaskMarked(task);
    }

    /**
     * Marks the specified task as not done.
     *
     * @param taskIndexInteger 1-based index of the task to unmark.
     * @return The message string indicating if the task has been unmarked.
     * @throws AristoException If the task is already not done or index is invalid.
     */
    public String handleUnmarkTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.getTask(taskIndexInteger);

        if (!task.isDone()) {
            throw new AristoException("Task " + taskIndexInteger + " is already marked as not done.\n");
        }

        task.markAsNotDone();
        storage.saveTasks(taskList);
        return ui.showTaskUnmarked(task);
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param taskIndexInteger 1-based index of the task to delete.
     * @return The message string indicating if the task has been deleted.
     * @throws AristoException If the task index is invalid.
     */
    public String handleDeleteTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.removeTask(taskIndexInteger);
        storage.saveTasks(taskList);
        return ui.showTaskDeleted(task) + this.printNumberOfTasks();
    }

    /**
     * Adds a new todo task to the task list.
     *
     * @param description Description of the todo task.
     * @return The message string indicating if the task has been added.
     * @throws AristoException If the description is blank.
     */
    public String handleTodo(String description) throws AristoException {
        if (description.isBlank()) {
            throw new AristoException("Task description is empty, please retry with a valid task description.\n");
        }

        Todo todoTask = new Todo(description);
        taskList.addTask(todoTask);
        storage.saveTasks(taskList);
        return ui.showTodoTaskAdded(todoTask) + this.printNumberOfTasks();
    }

    /**
     * Adds a new deadline task to the task list.
     *
     * @param taskDetails Description and deadline (format: "DESCRIPTION /by DEADLINE").
     * @return The message string indicating if the task has been added.
     * @throws AristoException If input is blank or deadline format is invalid.
     */
    public String handleDeadline(String taskDetails) throws AristoException {
        if (taskDetails.isBlank()) {
            throw new AristoException("Please provide a task description and its deadline! e.g XXX /by YYY\n");
        }

        Deadline deadlineTask = getDeadline(taskDetails);
        taskList.addTask(deadlineTask);
        storage.saveTasks(taskList);
        return ui.showDeadlineTaskAdded(deadlineTask) + this.printNumberOfTasks();
    }

    /**
     * Parses and returns a <code>Deadline</code> task from input string.
     *
     * @param taskDetails Input string containing description and deadline.
     * @return Deadline task object.
     * @throws AristoException If date format is invalid.
     */
    private static Deadline getDeadline(String taskDetails) throws AristoException {
        String[] taskComponents = Parser.parseDeadline(taskDetails);

        String description = taskComponents[0];
        String deadline = taskComponents[1];

        try {
            return new Deadline(description, deadline);
        } catch (DateTimeParseException e) {
            throw new AristoException("Invalid date format! Please enter a valid date in the format yyyy-MM-dd.\n");
        }
    }

    /**
     * Adds a new <code>Event</code> task to the task list.
     *
     * @param taskDetails Description, start and end times (format: "DESCRIPTION /from START /to END").
     * @return The message string indicating if the event has been added.
     * @throws AristoException If input is missing description or times, or has invalid date format.
     */
    public String handleEvent(String taskDetails) throws AristoException {
        String[] taskComponents = Parser.parseEvent(taskDetails);
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
            return ui.showEventTaskAdded(eventTask) + this.printNumberOfTasks();
        } catch (DateTimeParseException e) {
            throw new AristoException("Please use yyyy-MM-dd format for dates!\n");
        }
    }
}
