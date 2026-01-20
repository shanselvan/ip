import java.util.Scanner;

public class Aristo {
    private static final Task[] taskList = new Task[100];
    private static int numberOfTasks = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Aristo.greet();
        String userInput = scanner.nextLine();

        while (!userInput.equals("bye")) {

            if (Aristo.isListCommand(userInput)) {
                Aristo.printTasks();
            } else if (Aristo.isMarkCommand(userInput)) {
                int taskIndex = Integer.parseInt(userInput.substring(5));
                Aristo.taskList[taskIndex - 1].markAsDone();
            } else if (Aristo.isUnmarkCommand(userInput)) {
                int taskIndex = Integer.parseInt(userInput.substring(7));
                Aristo.taskList[taskIndex - 1].markAsNotDone();
            } else {
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
        System.out.println();
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Hello, human!");
        System.out.println("Aristo here to assist. Fire away!");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public static void exit() {
        System.out.println();
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Goodbye!");
        System.out.println("Aristo eagerly awaits your return...");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public static boolean isMarkCommand(String userInput) {
        return userInput.startsWith("mark ");
    }

    public static boolean isUnmarkCommand(String userInput) {
        return userInput.startsWith("unmark ");
    }

    public static boolean isListCommand(String userInput) {
        return userInput.equals("list");
    }

    public static void printTasks() {
        for (int taskIndex = 0; taskIndex < Aristo.numberOfTasks; taskIndex++) {
            Task currentTask = Aristo.taskList[taskIndex];
            System.out.printf("%d.[%s] %s\n", taskIndex + 1, currentTask.getStatusIcon(), currentTask.description);
        }
    }
}
