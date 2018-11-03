package org.equinox.model.domain;

import org.codehaus.jackson.annotate.JsonCreator;
import org.equinox.exception.InvalidReportReasonException;

public enum ReportReason {
    ABUSIVE_CONTENT,
    SHOCK_CONTENT,
    ILLEGAL_CONTENT,
    WEBSITE_RULES_VIOLATION,
    SPAM,
    FLOOD;

    @JsonCreator
    public static ReportReason fromString(String string) {
        switch (string.toLowerCase()) {
            case "abusive_content":
                return ABUSIVE_CONTENT;
            case "shock_content":
                return SHOCK_CONTENT;
            case "illegal_content":
                return ILLEGAL_CONTENT;
            case "spam":
                return SPAM;
            case "website_rules_violation":
                return WEBSITE_RULES_VIOLATION;
            case "flood":
                return FLOOD;
            default:
                throw new InvalidReportReasonException("Invalid report reason, expected " +
                        "\"abusive_content\", \"illegal_content\", \"shock_content\", \"spam\", " +
                        "\"website_rules_violation\", \"flood\", got " + string);
        }
    }
}
