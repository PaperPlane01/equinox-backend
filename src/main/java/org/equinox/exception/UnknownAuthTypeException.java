package org.equinox.exception;

public class UnknownAuthTypeException extends RuntimeException {
    public UnknownAuthTypeException() {
    }

    public UnknownAuthTypeException(String message) {
        super(message);
    }

    public UnknownAuthTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
