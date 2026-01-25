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
}
