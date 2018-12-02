package aphelion.exception;

public class NoMatchFoundForGivenUsernameAndPasswordException extends RuntimeException {
    public NoMatchFoundForGivenUsernameAndPasswordException() {
    }

    public NoMatchFoundForGivenUsernameAndPasswordException(String message) {
        super(message);
    }

    public NoMatchFoundForGivenUsernameAndPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
