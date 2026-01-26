package aristo.parser;

import aristo.exception.AristoException;

public class Parser {

    public static String[] parseCommand(String userInput) {
        String[] parsedUserInput = userInput.split(" ", 2);
        String command = parsedUserInput[0];
        String taskIndexString = (parsedUserInput.length == 1)
                ? ""
                : parsedUserInput[1];

        return new String[]{command, taskIndexString};
    }

    public static int parseTaskIndex(String taskIndexString) throws AristoException {
         try {
             return Integer.parseInt(taskIndexString);
         } catch (NumberFormatException e) {
             throw new AristoException("Please specify a task number to mark as done!\n");
         }
    }

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
