package org.equinox.exception;

public class InvalidSortingDirectionException extends RuntimeException {
    public InvalidSortingDirectionException() {
    }

    public InvalidSortingDirectionException(String message) {
        super(message);
    }

    public InvalidSortingDirectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
