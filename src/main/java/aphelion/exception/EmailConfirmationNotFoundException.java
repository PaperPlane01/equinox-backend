package aphelion.exception;

public class EmailConfirmationNotFoundException extends EntityNotFoundException {
    public EmailConfirmationNotFoundException() {
    }

    public EmailConfirmationNotFoundException(String message) {
        super(message);
    }

    public EmailConfirmationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
