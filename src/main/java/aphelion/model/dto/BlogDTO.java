package aphelion.model.dto;

import aphelion.model.domain.BlogManagersVisibilityLevel;
import aphelion.model.domain.BlogPostPublisherType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Blog", description = "Object representing a blog")
public class BlogDTO {
    @ApiModelProperty("Id of blog")
    private Long id;
    @ApiModelProperty("Name of blog")
    private String name;
    @ApiModelProperty("Blog's owner")
    private UserMinifiedDTO owner;
    @ApiModelProperty("Description of blog")
    private String description;
    @ApiModelProperty("Creation date")
    private Date createdAt;
    @ApiModelProperty("Link to blog's avatar")
    private String avatarUri;
    @ApiModelProperty("Color of blog's letter avatar")
    private String letterAvatarColor;
    @ApiModelProperty("Indicates if current user subscribed to this blog")
    private boolean currentUserSubscribed;
    @ApiModelProperty("Id of current user's subscription to this blog.\n" +
            "If current user is not subscribed to blog, then the value is <code>null</code>")
    private Long subscriptionId;
    @ApiModelProperty("Visibility of blog managers")
    private BlogManagersVisibilityLevel blogManagersVisibilityLevel;
    @ApiModelProperty("Default publisher type")
    private BlogPostPublisherType defaultPublisherType;
}
