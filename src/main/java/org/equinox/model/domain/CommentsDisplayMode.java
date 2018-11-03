package org.equinox.model.domain;

import org.equinox.exception.InvalidCommentsDisplayModeException;

public enum CommentsDisplayMode {
    FLAT,
    ROOT_COMMENT;

    public static CommentsDisplayMode fromString(String commentsDisplayMode) {
        switch (commentsDisplayMode.toUpperCase()) {
            case "FLAT":
                return FLAT;
            case "ROOT_COMMENT":
                return ROOT_COMMENT;
            case "ROOTCOMMENT":
                return ROOT_COMMENT;
            default:
                throw new InvalidCommentsDisplayModeException("Unknown comments display mode, expected flat, root_comment or rootComment, got " + commentsDisplayMode);
        }
    }
}
