package org.equinox.exception;

public class InvalidPageNumberException extends RuntimeException {
    public InvalidPageNumberException() {
    }

    public InvalidPageNumberException(String message) {
        super(message);
    }

    public InvalidPageNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
