package aphelion.exception;

public class BlogPostIsTooLongException extends RuntimeException {
    public BlogPostIsTooLongException() {
    }

    public BlogPostIsTooLongException(String message) {
        super(message);
    }

    public BlogPostIsTooLongException(String message, Throwable cause) {
        super(message, cause);
    }
}
