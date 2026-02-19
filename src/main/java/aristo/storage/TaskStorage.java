package aristo.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import aristo.exception.AristoException;
import aristo.task.Deadline;
import aristo.task.Event;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.task.Todo;

/**
 * Handles loading and saving of tasks to a storage file.
 * <p>
 * This class is responsible for reading tasks from a file into memory
 * and saving tasks from memory back to the file. It ensures the data
 * file exists and can create directories as needed.
 */

public class TaskStorage {
    private final Path filePath;
    private static final String DELIMITER = " \\| ";
    private static final int MIN_FIELDS = 3;
    private static final int TYPE_INDEX = 0;
    private static final int DONE_INDEX = 1;
    private static final int DESC_INDEX = 2;

    /**
     * Constructs a TaskStorage object for the given file path.
     * Ensures that the data file and its parent directories exist.
     *
     * @param filePath the path to the storage file
     */
    public TaskStorage(String filePath) {
        this.filePath = Paths.get(filePath);

        try {
            ensureDataFileExists();
        } catch (IOException e) {
            System.out.println("Failed to create data file! " + e.getMessage());
        }
    }

    /**
     * Ensures that the data file used for storing tasks exists.
     * <p>
     * This method checks if the file at {@link #filePath} exists.
     * If the file does not exist, it creates the necessary directories
     * and an empty file.
     * </p>
     *
     * @throws IOException if an I/O error occurs while creating the directories or file.
     */
    private void ensureDataFileExists() throws IOException {
        Path parentDir = filePath.getParent();

        if (!Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Loads tasks from the storage file into a list of <code>Task</code> objects.
     * <p>
     * Tasks are parsed according to their type: <code>Todo</code>, <code>Deadline</code> or <code>Event</code>.
     * Completed tasks are marked as done.
     * </p>
     *
     * @return List of tasks loaded from the file.
     */
    public ArrayList<Task> loadTasksFromFile() {
        ArrayList<Task> loadedTasks = new ArrayList<>();

        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (isEmptyOrComment(line)) {
                    continue;
                }

                try {
                    Task task = parseTaskFromLine(line);
                    if (task != null) {
                        loadedTasks.add(task);
                    }
                } catch (AristoException e) {
                    System.out.println("Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading tasks! " + e.getMessage());
        }

        return loadedTasks;
    }

    private boolean isEmptyOrComment(String line) {
        return line.isEmpty() || line.startsWith("#");
    }

    private boolean isValidTaskFormat(String[] parts) {
        return parts.length >= MIN_FIELDS;
    }

    private boolean parseIsDone(String doneField) throws AristoException {
        if (doneField.equals("1")) {
            return true;
        } else if (doneField.equals("0")) {
            return false;
        } else {
            throw new AristoException("Task's done status is invalid!");
        }
    }

    private Task parseTaskFromLine(String line) throws AristoException {
        String[] parts = line.split(DELIMITER);

        if (!isValidTaskFormat(parts)) {
            return null;
        }

        String taskType = parts[TYPE_INDEX];
        boolean isDone = parseIsDone(parts[DONE_INDEX]);
        String description = parts[DESC_INDEX];

        Task task = createTaskByType(taskType, parts, description);

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Creates a Task object based on type and parts array.
     */
    private Task createTaskByType(String taskType, String[] parts, String description) throws AristoException {
        switch (taskType) {
        case "T":
            return new Todo(description);

        case "D":
            if (parts.length >= 4) {
                return new Deadline(description, parts[3]);
            }
            return null;

        case "E":
            String from = (parts.length > 3) ? parts[3] : "";
            String to = (parts.length > 4) ? parts[4] : "";
            return new Event(description, from, to);

        default:
            return null;
        }
    }

    /**
     * Saves the given task list into the storage file.
     * <p>
     * Each Task is converted to its file string representation before writing to the file.
     * </p>
     *
     * @param taskList TaskList containing the tasks to be saved.
     */
    public void saveTasksToFile(TaskList taskList) {
        try {
            ArrayList<String> lines = convertTasksToFileFormat(taskList);
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Error writing to data file! " + e.getMessage());
        }
    }

    /**
     * Converts a TaskList into an ArrayList of Strings.
     */
    private ArrayList<String> convertTasksToFileFormat(TaskList taskList) {
        ArrayList<String> lines = new ArrayList<>();
        for (Task task : taskList.asList()) {
            lines.add(task.toFileString());
        }
        return lines;
    }
}
