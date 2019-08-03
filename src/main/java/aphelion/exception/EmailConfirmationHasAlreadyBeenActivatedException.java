package aphelion.exception;

public class EmailConfirmationHasAlreadyBeenActivatedException extends RuntimeException {
    public EmailConfirmationHasAlreadyBeenActivatedException() {
    }

    public EmailConfirmationHasAlreadyBeenActivatedException(String message) {
        super(message);
    }

    public EmailConfirmationHasAlreadyBeenActivatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
