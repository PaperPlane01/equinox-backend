package aphelion.exception;

public class InvalidBlogManagersVisibilityLevelException extends RuntimeException {
    public InvalidBlogManagersVisibilityLevelException() {
    }

    public InvalidBlogManagersVisibilityLevelException(String message) {
        super(message);
    }

    public InvalidBlogManagersVisibilityLevelException(String message, Throwable cause) {
        super(message, cause);
    }
}
