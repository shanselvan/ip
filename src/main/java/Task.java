public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        this.isDone = true;
        System.out.printf("""
                Great job! I have marked this task as done.
                [%s] %s
                """, this.getStatusIcon(), this.description);
    }

    public void markAsNotDone() {
        this.isDone = false;
        System.out.printf("""
                Alright, I have marked this task as not done yet.
                [%s] %s
                """, this.getStatusIcon(), this.description);
    }
}
