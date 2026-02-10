package aristo;

import aristo.command.Command;
import aristo.command.CommandConstructor;
import aristo.exception.AristoException;
import aristo.parser.Parser;
import aristo.storage.TaskStorage;
import aristo.task.TaskList;
import aristo.ui.Ui;

/**
 * Represents the main chatbot application, Aristo, which manages the task list.
 */
public class Aristo {
    private TaskList taskList;
    private final Ui ui;
    private final TaskStorage storage;

    public Aristo() {
        this.ui = new Ui();
        this.storage = new TaskStorage("./data/aristo.txt");
        this.taskList = new TaskList(storage.loadTasks());
    }

    /**
     * The main entry point of the Aristo chatbot application.
     * Initializes storage and task list, greets the user, and enters the command loop.
     * Processes commands until the user types "bye", after which the application exits.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Aristo aristo = new Aristo();
        String userInput = aristo.ui.fetchNextCommand();

        while (!userInput.equals("bye")) {
            aristo.getResponse(userInput);
            userInput = aristo.ui.fetchNextCommand();
        }

        aristo.ui.exit();
    }

    public String getResponse(String input) {
        if (input.equals("greet")) {
            return ui.greet();
        }

        if (input.equals("bye")) {
            return ui.exit();
        }

        this.taskList = new TaskList(storage.loadTasks());

        try {
            String[] parsed = Parser.parseCommand(input);
            String command = parsed[0];
            String argument = parsed[1];

            CommandConstructor commandMaker = new CommandConstructor(taskList, storage, ui);
            Command userCommand = commandMaker.createCommand(command);
            return userCommand.execute(argument);

        } catch (AristoException e) {
            return ui.showError(e.getMessage());
        }
    }
}
