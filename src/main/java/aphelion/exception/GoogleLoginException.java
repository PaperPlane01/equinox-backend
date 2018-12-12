package aphelion.exception;

public class GoogleLoginException extends RuntimeException {
    public GoogleLoginException() {
    }

    public GoogleLoginException(String message) {
        super(message);
    }

    public GoogleLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
