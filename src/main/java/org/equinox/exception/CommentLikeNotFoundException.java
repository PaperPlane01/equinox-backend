package org.equinox.exception;

public class CommentLikeNotFoundException extends EntityNotFoundException {
    public CommentLikeNotFoundException() {
    }

    public CommentLikeNotFoundException(String message) {
        super(message);
    }

    public CommentLikeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
