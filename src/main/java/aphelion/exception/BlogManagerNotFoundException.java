package aphelion.exception;

public class BlogManagerNotFoundException extends EntityNotFoundException {
    public BlogManagerNotFoundException() {
    }

    public BlogManagerNotFoundException(String message) {
        super(message);
    }

    public BlogManagerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
