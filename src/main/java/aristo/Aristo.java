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

    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_BYE = "bye";


    public static void main(String[] args) {
        TaskStorage storage = new TaskStorage("./data/aristo.txt");
        taskList = new TaskList(storage.loadTasks());

        ui.greet();
        String userInput = ui.fetchNextCommand();

        while (!userInput.equals(COMMAND_BYE)) {
            try {
                String[] parsed = Parser.parseCommand(userInput);
                String command = parsed[0];
                String taskIndexString = parsed[1];

                switch (command) {
                case COMMAND_LIST:
                    ui.printTaskList(taskList);
                    break;

                case COMMAND_MARK:
                    int taskIndexInteger = Parser.parseTaskIndex(taskIndexString);
                    Aristo.handleMarkTask(taskIndexInteger);
                    break;

                case COMMAND_UNMARK:
                    if (taskIndexString.isBlank()) {
                        throw new AristoException("Please specify a task number to unmark!\n");
                    }

                    int taskIndex = Integer.parseInt(taskIndexString);
                    Aristo.handleUnmarkTask(taskIndex);
                    break;

                case COMMAND_DELETE:
                    if (taskIndexString.isBlank()) {
                        throw new AristoException("Please specify a task number to delete!\n");
                    }

                    int taskIndexInt = Integer.parseInt(taskIndexString);
                    Aristo.handleDeleteTask(taskIndexInt);
                    break;

                case COMMAND_TODO:
                    Aristo.handleTodo(taskIndexString);
                    break;

                case COMMAND_DEADLINE:
                    Aristo.handleDeadline(taskIndexString);
                    break;

                case COMMAND_EVENT:
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

    public static void printNumberOfTasks() {
        ui.printNumberOfTasks(taskList);
    }

    public static void handleMarkTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.getTask(taskIndexInteger);

        if (task.isDone()) {
            throw new AristoException("Task " + taskIndexInteger + " has already been marked as done.\n");
        }

        task.markAsDone();
        ui.showTaskMarked(task);
    }

    public static void handleUnmarkTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.getTask(taskIndexInteger);

        if (!task.isDone()) {
            throw new AristoException("Task " + taskIndexInteger + " is already marked as not done.\n");
        }

        task.markAsNotDone();
        ui.showTaskUnmarked(task);
    }

    public static void handleDeleteTask(int taskIndexInteger) throws AristoException {
        Task task = taskList.removeTask(taskIndexInteger);
        ui.showTaskDeleted(task);
        Aristo.printNumberOfTasks();
    }

    public static void handleTodo(String description) throws AristoException {
        if (description.isBlank()) {
            throw new AristoException("Task description is empty, please retry with a valid task description.\n");
        }

        Todo todoTask = new Todo(description);
        taskList.addTask(todoTask);
        ui.showTodoTaskAdded(todoTask);
        Aristo.printNumberOfTasks();
    }

    public static void handleDeadline(String taskDetails) throws AristoException {
        if (taskDetails.isBlank()) {
            throw new AristoException("Please provide a task description and its deadline! e.g XXX /by YYY\n");
        }

        Deadline deadlineTask = getDeadline(taskDetails);
        taskList.addTask(deadlineTask);
        ui.showDeadlineTaskAdded(deadlineTask);
        Aristo.printNumberOfTasks();
    }

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