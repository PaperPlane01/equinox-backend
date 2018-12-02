package aphelion.exception;

public class InvalidVkTokenException extends RuntimeException {
    public InvalidVkTokenException() {
    }

    public InvalidVkTokenException(String message) {
        super(message);
    }

    public InvalidVkTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
