import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskStorage {
    private Path filePath;

    public TaskStorage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    private void ensureDataFileExists() throws IOException {
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.out.println("Could not initialise data file! " + e.getMessage());
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

                Task task;

                switch (taskType) {

                case "T":
                    task = new Todo(taskDescription);
                    break;

                case "D":
                    String by = parts[3];
                    task = new Deadline(taskDescription, by);
                    break;

                case "E":
                    String from = parts[3];
                    String to = parts[4];
                    task = new Event(taskDescription, from, to);
                    break;

                default:
                    continue;

                }

                if (isDone) {
                    task.markAsDone();
                }

                loadedTasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading tasks! " + e.getMessage());
        }

        return loadedTasks;
    }
}
