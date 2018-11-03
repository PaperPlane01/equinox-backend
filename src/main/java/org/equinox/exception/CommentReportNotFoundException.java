package org.equinox.exception;

public class CommentReportNotFoundException extends EntityNotFoundException {
    public CommentReportNotFoundException() {
    }

    public CommentReportNotFoundException(String message) {
        super(message);
    }

    public CommentReportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
