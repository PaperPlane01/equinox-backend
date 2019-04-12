package aphelion.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonCreator;
import aphelion.exception.InvalidBlogManagersVisibilityLevelException;

@ApiModel(value = "Blog managers visibility level", description = "Level of blog managers' visibility")
public enum BlogManagersVisibilityLevel {
    @ApiModelProperty("Anyone can see managers of this blog")
    PUBLIC,
    @ApiModelProperty("Only registered users can see managers of this blog")
    REGISTERED_USERS,
    @ApiModelProperty("Only blog's subscribers can see managers of this blog")
    SUBSCRIBERS,
    @ApiModelProperty("Only other managers of this blog can see managers of this blog")
    MANAGERS,
    @ApiModelProperty("Only blog's owner can see managers of this blog")
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
