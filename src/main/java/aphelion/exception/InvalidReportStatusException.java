package aphelion.exception;

public class InvalidReportStatusException extends RuntimeException {
    public InvalidReportStatusException() {
    }

    public InvalidReportStatusException(String message) {
        super(message);
    }

    public InvalidReportStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
