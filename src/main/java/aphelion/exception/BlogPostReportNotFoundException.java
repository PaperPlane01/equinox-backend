package aphelion.exception;

public class BlogPostReportNotFoundException extends EntityNotFoundException {
    public BlogPostReportNotFoundException() {
    }

    public BlogPostReportNotFoundException(String message) {
        super(message);
    }

    public BlogPostReportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
