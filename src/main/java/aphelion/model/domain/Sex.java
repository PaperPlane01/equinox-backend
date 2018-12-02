package aphelion.model.domain;

import aphelion.exception.UnknownSexException;
import org.codehaus.jackson.annotate.JsonCreator;

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
