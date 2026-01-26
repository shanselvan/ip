package aristo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDate by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDate.parse(by, INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }

    @Override
    public String toFileString() {
        int isDone = this.isDone ? 1 : 0;
        return "D | " + isDone + " | " + this.description + " | " + by.format(INPUT_FORMATTER);
    }
}
