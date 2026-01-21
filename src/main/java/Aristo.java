import java.util.Scanner;

public class Aristo {
    private static final Task[] taskList = new Task[100];
    private static int numberOfTasks = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Aristo.greet();
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {
            String[] parsedUserInput = userInput.split(" ", 2);
            String command = parsedUserInput[0];
            String taskIndexString =
                    parsedUserInput.length == 1
                    ? ""
                    : parsedUserInput[1];

            switch (command) {
                case "list":
                    Aristo.printTasks();
                    break;

                case "mark":
                    int taskIndexInteger = Integer.parseInt(taskIndexString);
                    Aristo.handleMarkTask(taskIndexInteger);
                    break;

                case "unmark":
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
                    System.out.println("=======================================================");
                    System.out.println("I have added the task: " + userInput);
                    Aristo.taskList[Aristo.numberOfTasks] = new Task(userInput);
                    Aristo.numberOfTasks++;
                    System.out.println("=======================================================");
                    System.out.println();

            }
            userInput = scanner.nextLine();
        }
        Aristo.exit();
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

    public static void printTasks() {
        for (int taskIndex = 0; taskIndex < Aristo.numberOfTasks; taskIndex++) {
            Task currentTask = Aristo.taskList[taskIndex];
            System.out.printf("%d. %s\n", taskIndex + 1, currentTask);
        }
        System.out.println();
    }

    public static void handleMarkTask(int taskIndexInteger) {
        Task task = Aristo.taskList[taskIndexInteger - 1];
        task.markAsDone();
        System.out.printf("""
                Great job! I have marked this task as done.
                %s
                
                """, task);
    }

    public static void handleUnmarkTask(int taskIndexInteger) {
        Task task = Aristo.taskList[taskIndexInteger - 1];
        task.markAsNotDone();
        System.out.printf("""
                Alright, I have marked this task as not done yet.
                %s
                
                """, task);
    }

    public static void handleTodo(String description) {
        Todo todoTask = new Todo(description);
        Aristo.taskList[Aristo.numberOfTasks] = todoTask;
        Aristo.numberOfTasks++;
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                There are %d tasks in your list now.
                
                """, todoTask, Aristo.numberOfTasks);

    }

    public static void handleDeadline(String taskDetails) {
        String[] taskComponents = taskDetails.split(" /by ", 2);
        String description = taskComponents[0];
        String deadline = taskComponents[1];

        Deadline deadlineTask = new Deadline(description, deadline);
        Aristo.taskList[Aristo.numberOfTasks] = deadlineTask;
        Aristo.numberOfTasks++;
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                There are %d tasks in your list now.
                
                """, deadlineTask, Aristo.numberOfTasks);
    }

    public static void handleEvent(String taskDetails) {
        String[] taskComponents = taskDetails.split(" /from ", 2);
        String description = taskComponents[0];
        String fromAndTo = taskComponents[1];

        String[] fromToComponents = fromAndTo.split(" /to ", 2);
        String from = fromToComponents[0];
        String to = fromToComponents[1];

        Event eventTask = new Event(description, from, to);
        Aristo.taskList[Aristo.numberOfTasks] = eventTask;
        Aristo.numberOfTasks++;
        System.out.printf("""
                Noted, I have added this event to your list:
                %s
                There are %d tasks in your list now.
                
                """, eventTask, Aristo.numberOfTasks);
    }
}
