package org.equinox.model.domain;

import org.codehaus.jackson.annotate.JsonCreator;
import org.equinox.exception.UnknownSexException;

public enum Sex {
    MALE,
    FEMALE;

    @JsonCreator
    public static Sex fromString(String string) {
        switch (string.toLowerCase()) {
            case "male":
                return MALE;
            case "female":
                return FEMALE;
            default:
                throw new UnknownSexException("Unknown sex has been specified, expected \"male\" or " +
                        "\"female\", got " + string);
        }
    }
}
