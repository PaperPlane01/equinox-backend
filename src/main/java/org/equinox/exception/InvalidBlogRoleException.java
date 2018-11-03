package org.equinox.exception;

public class InvalidBlogRoleException extends RuntimeException {
    public InvalidBlogRoleException() {
    }

    public InvalidBlogRoleException(String message) {
        super(message);
    }

    public InvalidBlogRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
