package aphelion.exception;

public class InvalidPageSizeException extends RuntimeException {
    public InvalidPageSizeException() {
    }

    public InvalidPageSizeException(String message) {
        super(message);
    }

    public InvalidPageSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
