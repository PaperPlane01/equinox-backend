package aphelion.exception;

public class InvalidCollectionSizeException extends RuntimeException {
    public InvalidCollectionSizeException() {
    }

    public InvalidCollectionSizeException(String message) {
        super(message);
    }

    public InvalidCollectionSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
