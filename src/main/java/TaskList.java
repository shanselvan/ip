import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public Task removeTask(int index) throws AristoException {
        if (index > tasks.size() || index < 1) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }
        return tasks.remove(index - 1);
    }

    public Task getTask(int index) throws AristoException {
        if (index > tasks.size() || index < 1) {
            throw new AristoException("Invalid task number! Please retry with a valid task number.\n");
        }
        return tasks.get(index - 1);
    }

    public ArrayList<Task> asList() {
        return new ArrayList<>(tasks);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
