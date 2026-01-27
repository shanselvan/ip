package aristo.exception;

/**
 * Represents exceptions specific to the Aristo chatbot.
 * <p>
 * This exception is thrown when the user input is invalid or when
 * a task operation cannot be performed.
 */
public class AristoException extends Exception {

    public AristoException(String message) {
        super(message);
    }
}
