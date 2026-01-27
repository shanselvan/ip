package aristo.storage;

import aristo.task.Deadline;
import aristo.task.Event;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.task.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving of tasks to a storage file.
 * <p>
 * This class is responsible for reading tasks from a file into memory
 * and saving tasks from memory back to the file. It ensures the data
 * file exists and can create directories as needed.
 */
public class TaskStorage {
    private final Path filePath;

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
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \\| ");
                String taskType = parts[0];
                boolean isDone = parts[1].equals("1");
                String taskDescription = parts[2];

                Task task = null;

                switch (taskType) {

                case "T":
                    task = new Todo(taskDescription);
                    break;

                case "D":
                    String by = parts[3];
                    task = new Deadline(taskDescription, by);
                    break;

                case "E":
                    String from = (parts.length > 3) ? parts[3] : "";
                    String to = (parts.length > 4) ? parts[4] : "";
                    task = new Event(taskDescription, from, to);
                    break;

                default:
                    break;
                }

                if (task != null && isDone) {
                    task.markAsDone();
                }

                loadedTasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading tasks! " + e.getMessage());
        }

        return loadedTasks;
    }

    /**
     * Saves the given task list into the storage file.
     * <p>
     * Each Task is converted to its file string representation before writing to the file.
     * </p>
     *
     * @param taskList TaskList containing the tasks to be saved.
     */
    public void saveTasks(TaskList taskList) {
        try {
            ArrayList<Task> tasks = taskList.asList();
            ArrayList<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileString());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Error writing to data file! " + e.getMessage());
        }
    }
}
