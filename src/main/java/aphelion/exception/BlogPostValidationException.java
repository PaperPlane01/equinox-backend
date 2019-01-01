package aphelion.exception;

public class BlogPostValidationException extends RuntimeException {
    public BlogPostValidationException() {
    }

    public BlogPostValidationException(String message) {
        super(message);
    }

    public BlogPostValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogPostValidationException(Throwable cause) {
        super(cause);
    }
}
