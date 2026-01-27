package aristo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents an <code>Event</code> task in the Aristo task list.
 * <p>
 * An <code>Event</code> task contains a description, start time and end time.
 */
public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Constructs an Event task with a description, start date, and end date.
     *
     * @param description the description of the event
     * @param from the start date of the event in the format "yyyy-MM-dd"
     * @param to the end date of the event in the format "yyyy-MM-dd"
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDate.parse(from, INPUT_FORMATTER);
        this.to = LocalDate.parse(to, INPUT_FORMATTER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(OUTPUT_FORMATTER) +
                " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toFileString() {
        int isDone = this.isDone ? 1 : 0;
        return "E | " + isDone + " | " + this.description + " | " + from.format(INPUT_FORMATTER)
                + " | " + to.format(INPUT_FORMATTER);
    }
}
