package aphelion.model.domain;

import org.codehaus.jackson.annotate.JsonCreator;
import aphelion.exception.InvalidReportStatusException;

public enum  ReportStatus {
    ACCEPTED,
    DECLINED,
    NOT_VIEWED;

    @JsonCreator
    public static ReportStatus fromString(String reportStatus) {
        switch (reportStatus.toLowerCase()) {
            case "accepted":
                return ACCEPTED;
            case "declined":
                return DECLINED;
            case "not_viewed":
                return NOT_VIEWED;
            default:
                throw new InvalidReportStatusException("Invalid report status, expected \"accepted\", \"declined\" or \"not_viewed\", got " + reportStatus);
        }
    }
}