package aristo.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        String taskIndexString = getTaskIndexString(parsedUserInput);

        return new String[]{command, taskIndexString};
    }

    private static String getTaskIndexString(String[] parsedUserInput) {
        return (parsedUserInput.length == 1)
                ? ""
                : parsedUserInput[1];
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

        if (hasMissingParts(taskComponents)) {
            throw new AristoException("""
                Ensure you have included both the task description & deadline in the correct format.
                deadline <description> /by YYYY-MM-DD
                """
            );
        }

        String firstComponent = taskComponents[0].trim();
        String secondComponent = taskComponents[1].trim();

        if (isEmptyComponent(firstComponent) || isEmptyComponent(secondComponent)) {
            throw new AristoException("""
                Ensure you have included both the task description & deadline in the correct format.
                deadline <description> /by YYYY-MM-DD
                """
            );
        }

        assert taskComponents.length == 2 : "Parsed deadline String[] should have length 2";
        return new String[]{firstComponent, secondComponent};
    }

    private static boolean hasMissingParts(String[] taskComponents) {
        assert taskComponents != null : "Task components cannot be null";
        return taskComponents.length < 2;
    }

    private static boolean isEmptyComponent(String component) {
        return component.isBlank();
    }

    /**
     * Parses an <code>Event</code> task input into description, start time, and end time.
     *
     * @param taskDetails Input string containing description, start, and end times
     *     (format: "DESCRIPTION /from START /to END").
     * @return Array where index 0 is the description, index 1 is the start time, index 2 is the end time.
     * @throws AristoException If input is missing description, start, or end times.
     */
    public static String[] parseEvent(String taskDetails) throws AristoException {
        if (taskDetails == null || taskDetails.isBlank()) {
            throw new AristoException("Event what? Please provide the description, start and end dates.");
        }

        String[] taskComponents = taskDetails.split(" /from ", 2);
        String description = taskComponents[0];

        if (taskDetails.startsWith("/from") || description.isBlank()) {
            throw new AristoException("Event description is missing! Please provide a description before /from.");
        }

        if (taskComponents.length < 2 || taskComponents[1].isBlank()) {
            throw new AristoException("Double check you have the start and end times! Use: /from <START> /to <END>");
        }

        String[] fromAndTo = taskComponents[1].split(" /to ", 2);

        if (fromAndTo.length < 2) {
            throw new AristoException("Have you included both the start and end dates, spaced and ordered correctly?");
        }

        String fromComponent = fromAndTo[0].trim();
        String toComponent = fromAndTo[1].trim();

        if (fromComponent.isEmpty()) {
            throw new AristoException("Start date is missing! Please provide a start date after /from.");
        }
        if (toComponent.isEmpty()) {
            throw new AristoException("End date is missing! Please provide an end date after /to.");
        }

        try {
            Parser.parseDate(fromComponent);
        } catch (AristoException e) {
            throw new AristoException("Start date is sus... " + e.getMessage());
        }

        try {
            Parser.parseDate(toComponent);
        } catch (AristoException e) {
            throw new AristoException("End date looks sus... " + e.getMessage());
        }

        assert fromAndTo.length == 2 : "From and to components of parsed event must be present";
        return new String[]{description, fromComponent, toComponent};
    }

    /**
     * Parses a date string in the format YYYY-MM-DD.
     *
     * @param dateString String representing the date.
     * @return Parsed LocalDate object.
     * @throws AristoException If the input cannot be parsed as a valid date.
     */
    public static LocalDate parseDate(String dateString) throws AristoException {
        if (dateString == null || dateString.isBlank()) {
            throw new AristoException("The date cannot be empty. Please provide a date in YYYY-MM-DD format.");
        }

        if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new AristoException("Invalid date input: '" + dateString
                + "'. Please use YYYY-MM-DD (e.g., 2023-01-01).");
        }

        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        } catch (java.time.format.DateTimeParseException e) {
            throw new AristoException(
                    "Invalid date input: '" + dateString
                        + "'. Make sure the month is 01-12 and day is valid for the month."
            );
        }
    }
}
