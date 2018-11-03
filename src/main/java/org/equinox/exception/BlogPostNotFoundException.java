package org.equinox.exception;

public class BlogPostNotFoundException extends EntityNotFoundException {
    public BlogPostNotFoundException() {
    }

    public BlogPostNotFoundException(String message) {
        super(message);
    }

    public BlogPostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
