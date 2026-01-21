import java.util.Scanner;

public class Aristo {
    private static final Task[] taskList = new Task[100];
    private static int numberOfTasks = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Aristo.greet();
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {
            try {
                String[] parsedUserInput = userInput.split(" ", 2);
                String command = parsedUserInput[0];
                String taskIndexString =
                        parsedUserInput.length == 1
                                ? ""
                                : parsedUserInput[1];

                switch (command) {
                    case "list":
                        Aristo.printTaskList();
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
                            throw new AristoException("Please specify a task number to mark as done!\n");
                        }
                        int taskIndex = Integer.parseInt(taskIndexString);
                        Aristo.handleUnmarkTask(taskIndex);
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
                        throw new AristoException("Ehm, never heard of that command before!");
                }
            } catch (AristoException e) {
                System.out.println(e.getMessage());
            }
            userInput = scanner.nextLine();
        }
        Aristo.exit();
    }

    public static boolean isValidTaskNumber(int taskIndexInteger) {
        return taskIndexInteger > Aristo.numberOfTasks;
    }

    public static void greet() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Hello, human!");
        System.out.println("Aristo here to assist. Fire away!");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println();
    }

    public static void exit() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Goodbye!");
        System.out.println("Aristo eagerly awaits your return...");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public static void printTaskList() {
        if (Aristo.numberOfTasks == 0) {
            System.out.println("There are no tasks in your list.");
        } else {
            for (int taskIndex = 0; taskIndex < Aristo.numberOfTasks; taskIndex++) {
                Task currentTask = Aristo.taskList[taskIndex];
                System.out.printf("%d. %s\n", taskIndex + 1, currentTask);
            }
        }
        System.out.println();
    }

    public static void printNumberOfTasks() {
        if (Aristo.numberOfTasks == 1) {
            System.out.print("""
                There is 1 task in your list now.
                
                """);
        } else {
            System.out.printf("""
                There are %d tasks in your list now.
                
                """, Aristo.numberOfTasks);
        }
    }

    public static void handleMarkTask(int taskIndexInteger) throws AristoException {
        if (isValidTaskNumber(taskIndexInteger)) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }

        Task task = Aristo.taskList[taskIndexInteger - 1];

        if (task.isDone) {
            throw new AristoException("Task " + taskIndexInteger + " has already been marked as done.\n");
        }

        task.markAsDone();
        System.out.printf("""
                Great job! I have marked this task as done.
                %s
                
                """, task);
    }

    public static void handleUnmarkTask(int taskIndexInteger) throws AristoException {
        if (isValidTaskNumber(taskIndexInteger)) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }
        Task task = Aristo.taskList[taskIndexInteger - 1];

        if (!task.isDone) {
            throw new AristoException("Task " + taskIndexInteger + " is already marked as not done.\n");
        }

        task.markAsNotDone();
        System.out.printf("""
                Alright, I have marked this task as not done yet.
                %s
                
                """, task);
    }

    public static void handleTodo(String description) throws AristoException {
        if (description.isBlank()) {
            throw new AristoException("Task description is empty, please retry with a valid task description.\n");
        }

        Todo todoTask = new Todo(description);
        Aristo.taskList[Aristo.numberOfTasks] = todoTask;
        Aristo.numberOfTasks++;
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                """, todoTask);
        Aristo.printNumberOfTasks();

    }

    public static void handleDeadline(String taskDetails) throws AristoException {
        if (taskDetails.isBlank()) {
            throw new AristoException("Please provide a task description and its deadline! e.g XXX /by YYY\n");
        }

        Deadline deadlineTask = getDeadline(taskDetails);
        Aristo.taskList[Aristo.numberOfTasks] = deadlineTask;
        Aristo.numberOfTasks++;
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                """, deadlineTask);
        Aristo.printNumberOfTasks();
    }

    private static Deadline getDeadline(String taskDetails) throws AristoException {
        // System.out.println(taskDetails);
        String[] taskComponents = taskDetails.split(" /by ", 2);

        if (taskComponents.length != 2) {
            throw new AristoException("Ensure you have included both the task description & deadline! e.g XXX /by YYY\n");
        }
        // System.out.println(Arrays.toString(taskComponents));
        String description = taskComponents[0];
        String deadline = taskComponents[1];

        if (description.isBlank() || deadline.isBlank()) {
            throw new AristoException("Task description & deadline cannot be empty! Please try again.\n");
        }

        return new Deadline(description, deadline);
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
            throw new AristoException("Have you included the from and to times? e.g XXX /from YYY /to ZZZ\n");
        }

        String from = fromToComponents[0];
        String to = fromToComponents[1];

        if (from.isBlank()) {
            throw new AristoException("Please specify a from time!\n");
        } else if (to.isBlank()) {
            throw new AristoException("Please specify a to time!\n");
        }

        Event eventTask = new Event(description, from, to);
        Aristo.taskList[Aristo.numberOfTasks] = eventTask;
        Aristo.numberOfTasks++;
        System.out.printf("""
                Noted, I have added this event to your list:
                %s
                """, eventTask);
        Aristo.printNumberOfTasks();
    }
}
