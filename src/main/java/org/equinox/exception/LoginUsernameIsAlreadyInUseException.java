package org.equinox.exception;

public class LoginUsernameIsAlreadyInUseException extends RuntimeException {
    public LoginUsernameIsAlreadyInUseException() {
    }

    public LoginUsernameIsAlreadyInUseException(String message) {
        super(message);
    }

    public LoginUsernameIsAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
