package aphelion.exception;

public class PinnedBlogPostsLimitHasBeenReachedException extends RuntimeException {
    public PinnedBlogPostsLimitHasBeenReachedException() {
    }

    public PinnedBlogPostsLimitHasBeenReachedException(String message) {
        super(message);
    }

    public PinnedBlogPostsLimitHasBeenReachedException(String message, Throwable cause) {
        super(message, cause);
    }
}
