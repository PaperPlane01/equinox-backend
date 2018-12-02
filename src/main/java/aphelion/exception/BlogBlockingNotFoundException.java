package aphelion.exception;

public class BlogBlockingNotFoundException extends EntityNotFoundException {
    public BlogBlockingNotFoundException() {
    }

    public BlogBlockingNotFoundException(String message) {
        super(message);
    }

    public BlogBlockingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
