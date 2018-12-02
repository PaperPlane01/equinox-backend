package aphelion.exception;

public class GlobalBlockingNotFoundException extends EntityNotFoundException {
    public GlobalBlockingNotFoundException() {
    }

    public GlobalBlockingNotFoundException(String message) {
        super(message);
    }

    public GlobalBlockingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
