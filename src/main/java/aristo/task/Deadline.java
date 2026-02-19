package aristo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import aristo.exception.AristoException;
import aristo.parser.Parser;

/**
 * Represents a <code>Deadline</code> task in the Aristo task list.
 * <p>
 * A <code>Deadline</code> task contains a description and a deadline date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");
    protected LocalDate by;

    /**
     * Constructs a Deadline task with a description and a deadline date.
     *
     * @param description the description of the task
     * @param by the deadline date in the format "yyyy-MM-dd"
     */
    public Deadline(String description, String by) throws AristoException {
        super(description);
        this.by = Parser.parseDate(by);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toFileString() {
        int isDone = this.isDone ? 1 : 0;
        return "D | " + isDone + " | " + this.description + " | " + by.format(INPUT_FORMATTER);
    }

    @Override
    public boolean isOccurringOn(LocalDate date) {
        return by.equals(date);
    }
}
