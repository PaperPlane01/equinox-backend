package aphelion.exception;

public class InvalidSortByException extends RuntimeException {
    public InvalidSortByException() {
    }

    public InvalidSortByException(String message) {
        super(message);
    }

    public InvalidSortByException(String message, Throwable cause) {
        super(message, cause);
    }
}
