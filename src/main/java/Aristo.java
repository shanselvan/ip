import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Represents the main chatbot application, Aristo, which manages the task list.
 */
public class Aristo {
    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        TaskStorage storage = new TaskStorage("./data/aristo.txt");
        tasks.addAll(storage.loadTasks());

        ui.greet();
        String userInput = ui.fetchNextCommand();

        while (!userInput.equals("bye")) {
            try {
                String[] parsedUserInput = userInput.split(" ", 2);
                String command = parsedUserInput[0];
                String taskIndexString = (parsedUserInput.length == 1)
                        ? ""
                        : parsedUserInput[1];

                switch (command) {
                case "list":
                    ui.printTaskList(Aristo.tasks);
                    break;

                case "mark":
                    if (taskIndexString.isBlank()) {
                        throw new AristoException("Please specify a task number to mark as done!\n");
                    }
                    int taskIndexInteger = Integer.parseInt(taskIndexString);
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
                storage.saveTasks(tasks);

            } catch (AristoException e) {
                System.out.println(e.getMessage());
            }
            userInput = ui.fetchNextCommand();
        }
        ui.exit();
    }

    public static boolean isInvalidTaskNumber(int taskIndexInteger) {
        return (taskIndexInteger > tasks.size() || taskIndexInteger < 1);
    }

    public static void printNumberOfTasks() {
        ui.printNumberOfTasks(Aristo.tasks);
    }

    public static void checkIsValidTaskNumber(int taskIndexInteger) throws AristoException {
        if (isInvalidTaskNumber(taskIndexInteger)) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }
    }

    public static void handleMarkTask(int taskIndexInteger) throws AristoException {
        Aristo.checkIsValidTaskNumber(taskIndexInteger);

        Task task = tasks.get(taskIndexInteger - 1);

        if (task.isDone) {
            throw new AristoException("Task " + taskIndexInteger + " has already been marked as done.\n");
        }

        task.markAsDone();
        ui.showTaskMarked(task);
    }

    public static void handleUnmarkTask(int taskIndexInteger) throws AristoException {
        Aristo.checkIsValidTaskNumber(taskIndexInteger);

        Task task = tasks.get(taskIndexInteger - 1);

        if (!task.isDone) {
            throw new AristoException("Task " + taskIndexInteger + " is already marked as not done.\n");
        }

        task.markAsNotDone();
        ui.showTaskUnmarked(task);
    }

    public static void handleDeleteTask(int taskIndexInteger) throws AristoException {
        Aristo.checkIsValidTaskNumber(taskIndexInteger);

        Task task = tasks.remove(taskIndexInteger - 1);
        ui.showTaskDeleted(task);
        Aristo.printNumberOfTasks();
    }

    public static void handleTodo(String description) throws AristoException {
        if (description.isBlank()) {
            throw new AristoException("Task description is empty, please retry with a valid task description.\n");
        }

        Todo todoTask = new Todo(description);
        tasks.add(todoTask);
        ui.showTodoTaskAdded(todoTask);
        Aristo.printNumberOfTasks();

    }

    public static void handleDeadline(String taskDetails) throws AristoException {
        if (taskDetails.isBlank()) {
            throw new AristoException("Please provide a task description and its deadline! e.g XXX /by YYY\n");
        }

        Deadline deadlineTask = getDeadline(taskDetails);
        tasks.add(deadlineTask);
        ui.showDeadlineTaskAdded(deadlineTask);
        Aristo.printNumberOfTasks();
    }

    private static Deadline getDeadline(String taskDetails) throws AristoException {
        // System.out.println(taskDetails);
        String[] taskComponents = taskDetails.split(" /by ", 2);

        if (taskComponents.length != 2) {
            throw new AristoException("""
                Ensure you have included both the task description & deadline! e.g XXX /by YYY\n
                """
            );
        }
        // System.out.println(Arrays.toString(taskComponents));
        String description = taskComponents[0];
        String deadline = taskComponents[1];

        if (description.isBlank() || deadline.isBlank()) {
            throw new AristoException("Task description & deadline cannot be empty! Please try again.\n");
        }

        try {
            return new Deadline(description, deadline);
        } catch (DateTimeParseException e) {
            throw new AristoException("Invalid date format! Please enter a valid date in the format yyyy-MM-dd\n");
        }
    }

    public static void handleEvent(String taskDetails) throws AristoException {
        String[] taskComponents = taskDetails.split(" /from ", 2);

        if (taskComponents.length != 2) {
            throw new AristoException("Have you included the description & times? e.g XXX /from YYY /to ZZZ");
        }

        String description = taskComponents[0];
        String fromAndTo = taskComponents[1];

        if (description.isBlank()) {
            throw new AristoException("Please include the task description!\n");
        }

        String[] fromToComponents = fromAndTo.split(" /to ", 2);

        if (fromToComponents.length != 2) {
            throw new AristoException("Have you included the from and to times? e.g XXX /from YYY /to ZZZ.\n");
        }

        String from = fromToComponents[0];
        String to = fromToComponents[1];

        if (from.isBlank()) {
            throw new AristoException("Please specify a from time!\n");
        } else if (to.isBlank()) {
            throw new AristoException("Please specify a to time!\n");
        }

        try {
            Event eventTask = new Event(description, from, to);
            tasks.add(eventTask);
            ui.showEventTaskAdded(eventTask);
            Aristo.printNumberOfTasks();
        } catch (DateTimeParseException e) {
            throw new AristoException("Please use yyyy-MM-dd format for dates!\n");
        }
    }
}