package aphelion.exception;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException() {
    }

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
