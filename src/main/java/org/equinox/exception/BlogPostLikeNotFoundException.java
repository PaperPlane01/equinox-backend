package org.equinox.exception;

public class BlogPostLikeNotFoundException extends EntityNotFoundException {
    public BlogPostLikeNotFoundException() {
    }

    public BlogPostLikeNotFoundException(String message) {
        super(message);
    }

    public BlogPostLikeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
