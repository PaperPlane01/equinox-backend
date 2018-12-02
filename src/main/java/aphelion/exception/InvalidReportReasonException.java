package aphelion.exception;

public class InvalidReportReasonException extends RuntimeException {
    public InvalidReportReasonException() {
    }

    public InvalidReportReasonException(String message) {
        super(message);
    }

    public InvalidReportReasonException(String message, Throwable cause) {
        super(message, cause);
    }
}
