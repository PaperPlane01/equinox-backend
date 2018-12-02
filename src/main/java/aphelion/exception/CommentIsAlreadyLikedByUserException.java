package aphelion.exception;

public class CommentIsAlreadyLikedByUserException extends RuntimeException {
    public CommentIsAlreadyLikedByUserException() {
    }

    public CommentIsAlreadyLikedByUserException(String message) {
        super(message);
    }

    public CommentIsAlreadyLikedByUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
