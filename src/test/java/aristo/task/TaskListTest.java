package aristo.task;

import aristo.exception.AristoException;
import aristo.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

public class TaskListTest {

    @Test
    public void removeTask_validIndex_correctTaskRemoved() throws AristoException {
        ArrayList<Task> tasks = new ArrayList<>();
        Todo todo = new Todo("sleep");
        tasks.add(todo);
        Event event = new Event("party", "2023-01-01","2023-01-02");
        tasks.add(event);

        TaskList taskList = new TaskList(tasks);
        Task t1 = taskList.removeTask(2);

        assertEquals(t1, event);
        assertEquals(1, taskList.size());

        Task t2 = taskList.removeTask(1);

        assertEquals(t2, todo);
        assertEquals(0, taskList.size());
    }

    @Test
    public void removeTask_invalidIndex_throwsException() throws AristoException {
        ArrayList<Task> tasks = new ArrayList<>();
        Todo todo = new Todo("sleep");
        tasks.add(todo);
        Event event = new Event("party", "2023-01-01","2023-01-02");
        tasks.add(event);

        TaskList taskList = new TaskList(tasks);

        AristoException e1 = assertThrows(AristoException.class, () -> taskList.removeTask(3));
        assertTrue(
                e1.getMessage().contains(
                        "Invalid task number! Please retry with a valid task number."
                )
        );

        AristoException e2 = assertThrows(AristoException.class, () -> taskList.removeTask(0));
        assertTrue(
                e2.getMessage().contains(
                        "Invalid task number! Please retry with a valid task number."
                )
        );

    }

    @Test
    public void getTask_validIndex_correctTaskReturned() throws AristoException {
        ArrayList<Task> tasks = new ArrayList<>();
        Deadline deadline = new Deadline("homework", "2027-09-24");
        tasks.add(deadline);
        Event event = new Event("party", "2023-01-01","2023-01-02");
        tasks.add(event);

        TaskList taskList = new TaskList(tasks);
        Task t1 = taskList.getTask(1);

        assertEquals(t1, deadline);
        assertEquals(2, taskList.size());

        Task t2 = taskList.getTask(2);

        assertEquals(t2, event);
        assertEquals(2, taskList.size());
    }

    @Test
    public void getTask_invalidIndex_exceptionThrown() throws AristoException {
        ArrayList<Task> tasks = new ArrayList<>();
        Deadline deadline = new Deadline("homework", "2027-09-24");
        tasks.add(deadline);
        Event event = new Event("party", "2023-01-01","2023-01-02");
        tasks.add(event);

        TaskList taskList = new TaskList(tasks);

        AristoException e1 = assertThrows(AristoException.class, () -> taskList.getTask(0));
        assertTrue(
                e1.getMessage().contains(
                        "Invalid task number! Please retry with a valid task number."
                )
        );

        AristoException e2 = assertThrows(AristoException.class, () -> taskList.getTask(3));
        assertTrue(
                e2.getMessage().contains(
                        "Invalid task number! Please retry with a valid task number."
                )
        );
    }

}
