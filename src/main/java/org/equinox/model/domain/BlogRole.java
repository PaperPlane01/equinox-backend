package org.equinox.model.domain;

import org.codehaus.jackson.annotate.JsonCreator;
import org.equinox.exception.InvalidBlogRoleException;

public enum BlogRole {
    EDITOR,
    MODERATOR;

    @JsonCreator
    public static BlogRole fromString(String string) {
        switch (string.toLowerCase()) {
            case "editor":
                return EDITOR;
            case "moderator":
                return MODERATOR;
            default:
                throw new InvalidBlogRoleException("Invalid blog role, expected \"moderator\" or \"editor\", got " + string);
        }
    }
}
