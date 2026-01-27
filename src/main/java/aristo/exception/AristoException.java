package aristo.exception;

/**
 * Represents exceptions specific to the Aristo chatbot.
 * <p>
 * This exception is thrown when the user input is invalid or when
 * a task operation cannot be performed.
 */
public class AristoException extends Exception {

    /**
     * Constructs a new AristoException with the specified message.
     *
     * @param message the detail message for this exception
     */
    public AristoException(String message) {
        super(message);
    }
}
