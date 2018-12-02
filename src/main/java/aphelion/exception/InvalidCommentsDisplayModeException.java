package aphelion.exception;

public class InvalidCommentsDisplayModeException extends RuntimeException {
    public InvalidCommentsDisplayModeException() {
    }

    public InvalidCommentsDisplayModeException(String message) {
        super(message);
    }

    public InvalidCommentsDisplayModeException(String message, Throwable cause) {
        super(message, cause);
    }
}
