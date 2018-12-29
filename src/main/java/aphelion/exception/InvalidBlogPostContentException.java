package aphelion.exception;

public class InvalidBlogPostContentException extends RuntimeException {
    public InvalidBlogPostContentException() {
    }

    public InvalidBlogPostContentException(String message) {
        super(message);
    }

    public InvalidBlogPostContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
