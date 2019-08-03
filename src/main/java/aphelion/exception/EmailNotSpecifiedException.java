package aphelion.exception;

public class EmailNotSpecifiedException extends RuntimeException {
    public EmailNotSpecifiedException() {
    }

    public EmailNotSpecifiedException(String message) {
        super(message);
    }

    public EmailNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
