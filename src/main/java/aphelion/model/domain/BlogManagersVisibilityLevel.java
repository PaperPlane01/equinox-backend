package aphelion.model.domain;

import org.codehaus.jackson.annotate.JsonCreator;
import aphelion.exception.InvalidBlogManagersVisibilityLevelException;

public enum BlogManagersVisibilityLevel {
    PUBLIC,
    REGISTERED_USERS,
    SUBSCRIBERS,
    MANAGERS,
    OWNER;

    @JsonCreator
    public static BlogManagersVisibilityLevel fromString(String string) {
        if (string == null) {
            return PUBLIC;
        }

        switch (string.toLowerCase()) {
            case "public":
                return PUBLIC;
            case "registered_users":
                return REGISTERED_USERS;
            case "registeredusers":
                return REGISTERED_USERS;
            case "subscribers":
                return SUBSCRIBERS;
            case "managers":
                return MANAGERS;
            case "owner":
                return OWNER;
            default:
                throw new InvalidBlogManagersVisibilityLevelException("Invalid blog manages visibility " +
                        "level, expected public, registered_users, subscribers, managers, owner, got " +
                        "" + string);
        }
    }
}
