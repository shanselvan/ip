package aristo.ui;

import aristo.exception.AristoException;
import aristo.task.Deadline;
import aristo.task.Event;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.task.Todo;

import java.util.Scanner;

/**
 * Handles all user interaction for the Aristo chatbot.
 * <p>
 * This class is responsible for displaying messages to the user and
 * reading user input from standard input.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs an Ui object and initializes the input scanner.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the greeting message when Aristo starts.
     */
    public void greet() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Hello, human!");
        System.out.println("Aristo here to assist. Fire away!");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println();
    }

    /**
     * Displays the farewell message when Aristo exits.
     */
    public void exit() {
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Goodbye!");
        System.out.println("Aristo eagerly awaits your return...");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    /**
     * Prints all tasks currently stored in the task list.
     *
     * @param taskList The list of tasks to be printed.
     * @throws AristoException If an invalid task index is accessed.
     */
    public void printTaskList(TaskList taskList) throws AristoException {
        if (taskList.isEmpty()) {
            System.out.println("There are no tasks in your list.");
        } else {
            for (int taskIndex = 0; taskIndex < taskList.size(); taskIndex++) {
                Task currentTask = taskList.getTask(taskIndex + 1);
                System.out.printf("%d. %s\n", taskIndex + 1, currentTask);
            }
        }
        System.out.println();
    }

    /**
     * Prints the current number of tasks in the supplied task list.
     *
     */
    public void printNumberOfTasks(TaskList taskList) {
        int size = taskList.size();
        if (size == 1) {
            System.out.print("""
                There is 1 task in your list now.
                
                """);
        } else {
            System.out.printf("""
                There are %d tasks in your list now.
                
                """, size);
        }
    }

    /**
     * Reads and returns the next user command from standard input.
     *
     * @return The raw command entered by the user.
     */
    public String fetchNextCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays confirmation message to indicate a task is marked as complete.
     */
    public void showTaskMarked(Task task) {
        System.out.printf("""
                Great job! I have marked this task as done.
                %s
                
                """, task);
    }

    /**
     * Displays confirmation message to indicate a task is unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.printf("""
                Alright, I have marked this task as not done yet.
                %s
                
                """, task);
    }

    /**
     * Displays confirmation message to indicate a task has been removed from the list.
     */
    public void showTaskDeleted(Task task) {
        System.out.printf("""
                Okay, I have removed this task from your list:
                %s
                """, task);
    }

    /**
     * Displays confirmation message to indicate a {@link Todo} task has been added.
     */
    public void showTodoTaskAdded(Todo todoTask) {
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                """, todoTask);
    }

    /**
     * Displays confirmation message to indicate a {@link Deadline} task has been added.
     */
    public void showDeadlineTaskAdded(Deadline deadlineTask) {
        System.out.printf("""
                Noted, I have added this task to your list:
                %s
                """, deadlineTask);
    }

    /**
     * Displays confirmation message to indicate an {@link Event} task has been added.
     */
    public void showEventTaskAdded(Event eventTask) {
        System.out.printf("""
                    Noted, I have added this event to your list:
                    %s
                    """, eventTask);
    }

    /**
     * Displays an error message to the user.
     */
    public void showError(String errorMessage) {
        System.out.println(errorMessage);
    }
}
