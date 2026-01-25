import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void greet() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Hello, human!");
        System.out.println("Aristo here to assist. Fire away!");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println();
    }

    public void exit() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Goodbye!");
        System.out.println("Aristo eagerly awaits your return...");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public void printTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks in your list.");
        } else {
            for (int taskIndex = 0; taskIndex < tasks.size(); taskIndex++) {
                Task currentTask = tasks.get(taskIndex);
                System.out.printf("%d. %s\n", taskIndex + 1, currentTask);
            }
        }
        System.out.println();
    }

    public void printNumberOfTasks(ArrayList<Task> tasks) {
        if (tasks.size() == 1) {
            System.out.print("""
                There is 1 task in your list now.
                
                """);
        } else {
            System.out.printf("""
                There are %d tasks in your list now.
                
                """, tasks.size());
        }
    }

    public String fetchNextCommand() {
        return scanner.nextLine();
    }

    public void showTaskMarked(Task task) {
        System.out.printf("""
                Great job! I have marked this task as done.
                %s
                
                """, task);
    }

    public void showTaskUnmarked(Task task) {
        System.out.printf("""
                Alright, I have marked this task as not done yet.
                %s
                
                """, task);
    }

    public void showTaskDeleted(Task task) {
        System.out.printf("""
                Okay, I have removed this task from your list:
                %s
                """, task);
    }

    public void showTodoTaskAdded(Todo todoTask) {
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                """, todoTask);
    }

    public void showDeadlineTaskAdded(Deadline deadlineTask) {
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                """, deadlineTask);
    }

    public void showEventTaskAdded(Event eventTask) {
        System.out.printf("""
                    Noted, I have added this event to your list:
                    %s
                    """, eventTask);
    }

    public void showError(String errorMessage) {
        System.out.println(errorMessage);
    }
}
