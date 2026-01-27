package aristo.parser;

import aristo.exception.AristoException;

/**
 * Handles parsing of user input for the Aristo chatbot.
 * <p>
 * Provides static methods to parse commands, task indices, deadlines, and events.
 */
public class Parser {

    /**
     * Parses the user input into a command and its arguments.
     *
     * @param userInput Raw input string from the user.
     * @return Array where index 0 is the command, and index 1 is the rest of the input (or empty string if none).
     */
    public static String[] parseCommand(String userInput) {
        String[] parsedUserInput = userInput.split(" ", 2);
        String command = parsedUserInput[0];
        String taskIndexString = (parsedUserInput.length == 1)
                ? ""
                : parsedUserInput[1];

        return new String[]{command, taskIndexString};
    }

    /**
     * Parses a string as a 1-based task index.
     *
     * @param taskIndexString String representing the task index.
     * @return Parsed integer task index.
     * @throws AristoException If the input cannot be parsed as an integer.
     */
    public static int parseTaskIndex(String taskIndexString) throws AristoException {
         try {
             return Integer.parseInt(taskIndexString);
         } catch (NumberFormatException e) {
             throw new AristoException("Please specify a task number to mark as done!\n");
         }
    }

    /**
     * Parses a <code>Deadline</code> task input into description and deadline.
     *
     * @param taskDetails Input string containing task description and deadline (format: "DESCRIPTION /by DEADLINE").
     * @return Array where index 0 is the description, index 1 is the deadline.
     * @throws AristoException If input is missing a description or deadline.
     */
    public static String[] parseDeadline(String taskDetails) throws AristoException {
        String[] taskComponents = taskDetails.split(" /by ", 2);

        if (taskComponents.length != 2 || taskComponents[0].isBlank() || taskComponents[1].isBlank()) {
            throw new AristoException("""
                Ensure you have included both the task description & deadline! e.g XXX /by YYY\n
                """
            );
        }

        return taskComponents;
    }

    /**
     * Parses an <code>Event</code> task input into description, start time, and end time.
     *
     * @param taskDetails Input string containing description, start, and end times (format: "DESCRIPTION /from START /to END").
     * @return Array where index 0 is the description, index 1 is the start time, index 2 is the end time.
     * @throws AristoException If input is missing description, start, or end times.
     */
    public static String[] parseEvent(String taskDetails) throws AristoException {
        String[] taskComponents = taskDetails.split(" /from ", 2);

        if (taskComponents.length != 2 || taskComponents[0].isBlank()) {
            throw new AristoException("Have you included the description & times? e.g XXX /from YYY /to ZZZ.\n");
        }

        String description = taskComponents[0];
        String[] fromAndTo = taskComponents[1].split(" /to ", 2);

        if (fromAndTo.length != 2 || fromAndTo[0].isBlank() || fromAndTo[1].isBlank()) {
            throw new AristoException("Have you included the from and to times? e.g XXX /from YYY /to ZZZ.\n");
        }

        return new String[]{description, fromAndTo[0], fromAndTo[1]};
    }
}
