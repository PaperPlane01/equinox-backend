package aphelion.exception;

public class EmailIsAlreadyInUseException extends RuntimeException {
    public EmailIsAlreadyInUseException() {
    }

    public EmailIsAlreadyInUseException(String message) {
        super(message);
    }

    public EmailIsAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
