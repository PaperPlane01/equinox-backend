package aphelion.exception;

public class InvalidPublisherTypeException extends RuntimeException {
    public InvalidPublisherTypeException() {
    }

    public InvalidPublisherTypeException(String message) {
        super(message);
    }

    public InvalidPublisherTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
