import java.util.Arrays;
import java.util.Scanner;

public class Aristo {
    private static final String[] taskList = new String[100];
    private static int numberOfTasks = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Aristo.greet();
        String userInput = scanner.nextLine();
        while (!userInput.equals("bye")) {
            System.out.println("=======================================================");
            System.out.println("added: " + userInput);
            Aristo.taskList[Aristo.numberOfTasks] = userInput;
            Aristo.numberOfTasks++;
            System.out.println("=======================================================");
            System.out.println();
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
}
