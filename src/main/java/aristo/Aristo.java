package aristo;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.Deadline;
import aristo.task.Event;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.task.Todo;
import aristo.ui.Ui;

import java.time.format.DateTimeParseException;

/**
 * Represents the main chatbot application, Aristo, which manages the task list.
 */
public class Aristo {
    private static TaskList taskList;
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        TaskStorage storage = new TaskStorage("./data/aristo.txt");
        taskList = new TaskList(storage.loadTasks());

        ui.greet();
        String userInput = ui.fetchNextCommand();

        while (!userInput.equals("bye")) {
            try {
                String[] parsed = Parser.parseCommand(userInput);
                String command = parsed[0];
                String taskIndexString = parsed[1];

                switch (command) {
                case "list":
                    ui.printTaskList(taskList);
                    break;

                case "mark":
                    int taskIndexInteger = Parser.parseTaskIndex(taskIndexString);
                    Aristo.handleMarkTask(taskIndexInteger);
                    break;

                case "unmark":
                    if (taskIndexString.isBlank()) {
                        throw new AristoException("Please specify a task number to unmark!\n");
                    }
                    int taskIndex = Integer.parseInt(taskIndexString);
                    Aristo.handleUnmarkTask(taskIndex);
                    break;

                case "delete":
                    if (taskIndexString.isBlank()) {
                        throw new AristoException("Please specify a task number to delete!\n");
                    }
                    int taskIndexInt = Integer.parseInt(taskIndexString);
                    Aristo.handleDeleteTask(taskIndexInt);
                    break;

                case "todo":
                    Aristo.handleTodo(taskIndexString);
                    break;

                case "deadline":
                    Aristo.handleDeadline(taskIndexString);
                    break;

                case "event":
                    Aristo.handleEvent(taskIndexString);
                    break;

                default:
                    throw new AristoException("Ehm, never heard of that command before!\n");
                }
                storage.saveTasks(taskList);

            } catch (AristoException e) {
                ui.showError(e.getMessage());
            }
            userInput = ui.fetchNextCommand();
        }
        ui.exit();
    }

    /**
     * Displays the number of tasks currently in the task list.
     */
    public static void printNumberOfTasks() {
        ui.printNumberOfTasks(taskList);
    }


    /**
     * Marks the specified task as done.
     *
     * @param taskIndexInteger 1-based index of the task to mark.
     * @throws AristoException If the task is already marked as done or index is invalid.
     */
    public static void handleMarkTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.getTask(taskIndexInteger);

        if (task.isDone()) {
            throw new AristoException("Task " + taskIndexInteger + " has already been marked as done.\n");
        }

        task.markAsDone();
        ui.showTaskMarked(task);
    }

    /**
     * Marks the specified task as not done.
     *
     * @param taskIndexInteger 1-based index of the task to unmark.
     * @throws AristoException If the task is already not done or index is invalid.
     */
    public static void handleUnmarkTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.getTask(taskIndexInteger);

        if (!task.isDone()) {
            throw new AristoException("Task " + taskIndexInteger + " is already marked as not done.\n");
        }

        task.markAsNotDone();
        ui.showTaskUnmarked(task);
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param taskIndexInteger 1-based index of the task to delete.
     * @throws AristoException If the task index is invalid.
     */
    public static void handleDeleteTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.removeTask(taskIndexInteger);
        ui.showTaskDeleted(task);
        Aristo.printNumberOfTasks();
    }

    /**
     * Adds a new todo task to the task list.
     *
     * @param description Description of the todo task.
     * @throws AristoException If the description is blank.
     */
    public static void handleTodo(String description) throws AristoException {
        if (description.isBlank()) {
            throw new AristoException("Task description is empty, please retry with a valid task description.\n");
        }

        Todo todoTask = new Todo(description);
        taskList.addTask(todoTask);
        ui.showTodoTaskAdded(todoTask);
        Aristo.printNumberOfTasks();

    }

    /**
     * Adds a new deadline task to the task list.
     *
     * @param taskDetails Description and deadline (format: "DESCRIPTION /by DEADLINE").
     * @throws AristoException If input is blank or deadline format is invalid.
     */
    public static void handleDeadline(String taskDetails) throws AristoException {
        if (taskDetails.isBlank()) {
            throw new AristoException("Please provide a task description and its deadline! e.g XXX /by YYY\n");
        }

        Deadline deadlineTask = getDeadline(taskDetails);
        taskList.addTask(deadlineTask);
        ui.showDeadlineTaskAdded(deadlineTask);
        Aristo.printNumberOfTasks();
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
     * @throws AristoException If input is missing description or times, or has invalid date format.
     */
    public static void handleEvent(String taskDetails) throws AristoException {
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
            ui.showEventTaskAdded(eventTask);
            Aristo.printNumberOfTasks();
        } catch (DateTimeParseException e) {
            throw new AristoException("Please use yyyy-MM-dd format for dates!\n");
        }
    }
}