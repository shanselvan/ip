package aristo.parser;

import aristo.exception.AristoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    @Test
    public void parseCommand_basicInput_correctArray() {
        assertArrayEquals(
                new String[]{"mark", "2"},
                Parser.parseCommand("mark 2")
        );
        assertArrayEquals(
                new String[]{"list", ""},
                Parser.parseCommand("list ")
        );
        assertArrayEquals(
                new String[]{"todo", "eat fruits"},
                Parser.parseCommand("todo eat fruits")
        );
        assertArrayEquals(
                new String[]{"todo", ""},
                Parser.parseCommand("todo ")
        );
        assertArrayEquals(
                new String[]{"deadline", "revise /by 2025-01-04"},
                Parser.parseCommand("deadline revise /by 2025-01-04")
        );
        assertArrayEquals(
                new String[]{"event", "party /from 2024-02-01 /to 2025-01-04"},
                Parser.parseCommand("event party /from 2024-02-01 /to 2025-01-04")
        );
        assertArrayEquals(
                new String[]{"how", "do i enter a command?"},
                Parser.parseCommand("how do i enter a command?")
        );
    }

    @Test
    public void parseTaskIndex_validIndexString_correctIntegerReturned() throws AristoException {
        assertEquals(3, Parser.parseTaskIndex("3"));
        assertEquals(207, Parser.parseTaskIndex("207"));
    }

    @Test
    public void parseTaskIndex_invalidIndexString_exceptionThrown() throws AristoException {
        AristoException e1 = assertThrows(AristoException.class, () -> Parser.parseTaskIndex(" 67"));
        assertEquals(
                "Please specify a task number to mark as done!\n",
                e1.getMessage()
        );

        AristoException e2 = assertThrows(AristoException.class, () -> Parser.parseTaskIndex("nus"));
        assertEquals(
                "Please specify a task number to mark as done!\n",
                e2.getMessage()
        );

        AristoException e3 = assertThrows(AristoException.class, () -> Parser.parseTaskIndex(" "));
        assertEquals(
                "Please specify a task number to mark as done!\n",
                e3.getMessage()
        );

        AristoException e4 = assertThrows(AristoException.class, () -> Parser.parseTaskIndex("67 "));
        assertEquals(
                "Please specify a task number to mark as done!\n",
                e4.getMessage()
        );
    }

    @Test
    public void parseDeadline_validInput_correctArray() throws AristoException {
        assertArrayEquals(
                new String[]{"homework", "2024-01-01"},
                Parser.parseDeadline("homework /by 2024-01-01")
        );

        assertArrayEquals(
                new String[]{"eat     ", "2024-01-01"},
                Parser.parseDeadline("eat      /by 2024-01-01")
        );

        assertArrayEquals(
                new String[]{"eat", "   2024-01-01"},
                Parser.parseDeadline("eat /by    2024-01-01")
        );

        assertArrayEquals(
                new String[]{"eat", "  2pm"},
                Parser.parseDeadline("eat /by   2pm")
        );
    }

    @Test
    public void parseDeadline_invalidInput_exceptionThrown() throws AristoException {
        AristoException e1 = assertThrows(AristoException.class, () -> Parser.parseDeadline("eat /by"));
        assertTrue(
                e1.getMessage().contains(
                        "Ensure you have included both the task description & deadline!"
                )
        );

        AristoException e2 = assertThrows(AristoException.class, () -> Parser.parseDeadline("sleep /by  "));
        assertTrue(
                e2.getMessage().contains(
                        "Ensure you have included both the task description & deadline!"
                )
        );

        AristoException e3 = assertThrows(AristoException.class, () -> Parser.parseDeadline("sleep /byy"));
        assertTrue(
                e3.getMessage().contains(
                        "Ensure you have included both the task description & deadline!"
                )
        );
    }

}
