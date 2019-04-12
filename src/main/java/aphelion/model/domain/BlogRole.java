package aphelion.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonCreator;
import aphelion.exception.InvalidBlogRoleException;

@ApiModel(value = "Blog role", description = "Possible roles of blog managers")
public enum BlogRole {
    @ApiModelProperty(
            value = "Editor of blog.\n" +
                    "Blog editor can:\n" +
                    "<ul>" +
                    "<li>Create blog posts</li>" +
                    "<li>Delete own blog posts</li>" +
                    "<li>Block and unblock users in blog</li>" +
                    "<li>Delete and restore comments in blog/li>" +
                    "</ul>"
    )
    EDITOR,
    @ApiModelProperty(
            value = "Moderator of blog.\n" +
                    "Blog moderator can:\n" +
                    "<ul>" +
                    "<li>Block and unblock users in blog</li>" +
                    "<li>Delete and restore comments in blog</li>" +
                    "</ul>"
    )
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
