package aristo.ui;

import java.util.Scanner;

import aristo.exception.AristoException;
import aristo.task.Deadline;
import aristo.task.Event;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.task.Todo;

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
    public String greet() {
        String message = """
                Hello, human!
                Aristo here to assist. Fire away!
                
                """;
        System.out.print(message);
        return message;
    }

    /**
     * Displays the farewell message when Aristo exits.
     */
    public String exit() {
        String message = """
        Goodbye!
        Aristo eagerly awaits your return...
        """;
        System.out.print(message);
        return message;
    }

    /**
     * Prints all tasks currently stored in the task list.
     *
     * @param taskList The list of tasks to be printed.
     * @return the formatted string representation of the task list output
     * @throws AristoException If an invalid task index is accessed.
     */
    public String printTaskList(TaskList taskList) throws AristoException {
        StringBuilder output = new StringBuilder();

        if (taskList.isEmpty()) {
            output.append("There are no tasks in your list.\n\n");
        } else {
            for (int taskIndex = 0; taskIndex < taskList.size(); taskIndex++) {
                Task currentTask = taskList.getTask(taskIndex + 1);
                output.append(taskIndex + 1)
                        .append(". ")
                        .append(currentTask)
                        .append("\n");
            }
            output.append("\n");
        }

        String message = output.toString();
        System.out.print(message);
        return message;
    }

    /**
     * Prints all tasks from the given {@link TaskList} that match a certain keyword.
     * <p>
     * If the task list is empty, prints a message indicating that no tasks match.
     * Otherwise, prints the tasks in a numbered list.
     *
     * @param tasks the {@link TaskList} containing the matching tasks to print.
     * @return the formatted string representing the printed matching tasks
     * @throws AristoException if an error occurs while accessing the tasks
     */
    public String printMatchingTasks(TaskList tasks) throws AristoException {
        StringBuilder output = new StringBuilder();

        if (tasks.isEmpty()) {
            output.append("There are no tasks matching that keyword.\n\n");
        } else {
            output.append("Here are the matching tasks in your list:\n");

            for (int i = 1; i <= tasks.size(); i++) {
                output.append(i)
                        .append(". ")
                        .append(tasks.getTask(i))
                        .append("\n");
            }
            output.append("\n");
        }

        String message = output.toString();
        System.out.print(message);
        return message;
    }

    /**
     * Prints the current number of tasks in the supplied task list.
     *
     */
    public String printNumberOfTasks(TaskList taskList) {
        int size = taskList.size();
        String message;

        if (size == 1) {
            message = """
            There is 1 task in your list now.

            """;
        } else {
            message = String.format("""
            There are %d tasks in your list now.

            """, size);
        }

        System.out.print(message);
        return message;
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
    public String showTaskMarked(Task task) {
        String message = String.format("""
        Great job! I have marked this task as done.
        %s

        """, task);

        System.out.print(message);
        return message;
    }

    /**
     * Displays confirmation message to indicate a task is unmarked.
     */
    public String showTaskUnmarked(Task task) {
        String message = String.format("""
        Alright, I have marked this task as not done yet.
        %s

        """, task);

        System.out.print(message);
        return message;
    }

    /**
     * Displays confirmation message to indicate a task has been removed from the list.
     */
    public String showTaskDeleted(Task task) {
        String message = String.format("""
        Okay, I have removed this task from your list:
        %s
        """, task);

        System.out.print(message);
        return message;
    }

    /**
     * Displays confirmation message to indicate a {@link Todo} task has been added.
     */
    public String showTodoTaskAdded(Todo todoTask) {
        String message = String.format("""
        Noted, I have added this task to your list:
        %s
        """, todoTask);

        System.out.print(message);
        return message;
    }

    /**
     * Displays confirmation message to indicate a {@link Deadline} task has been added.
     */
    public String showDeadlineTaskAdded(Deadline deadlineTask) {
        String message = String.format("""
        Noted, I have added this task to your list:
        %s
        """, deadlineTask);

        System.out.print(message);
        return message;
    }

    /**
     * Displays confirmation message to indicate an {@link Event} task has been added.
     */
    public String showEventTaskAdded(Event eventTask) {
        String message = String.format("""
        Noted, I have added this task to your list:
        %s
        """, eventTask);

        System.out.print(message);
        return message;
    }

    /**
     * Displays an error message to the user.
     *
     */
    public String showError(String errorMessage) {
        System.out.println(errorMessage);
        return errorMessage;
    }
}
