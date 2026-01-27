package aristo.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import aristo.task.Deadline;
import aristo.task.Event;
import aristo.task.Task;
import aristo.task.TaskList;
import aristo.task.Todo;

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

    private void ensureDataFileExists() throws IOException {
        Path parentDir = filePath.getParent();

        if (!Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

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
