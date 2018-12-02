package aphelion.exception;

public class UnknownSexException extends RuntimeException {
    public UnknownSexException() {
    }

    public UnknownSexException(String message) {
        super(message);
    }

    public UnknownSexException(String message, Throwable cause) {
        super(message, cause);
    }
}
