package aphelion.exception;

public class EmailConfirmationExpiredException extends RuntimeException {
    public EmailConfirmationExpiredException() {
    }

    public EmailConfirmationExpiredException(String message) {
        super(message);
    }
}
