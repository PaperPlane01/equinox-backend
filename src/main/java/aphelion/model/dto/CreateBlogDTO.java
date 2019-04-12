package aphelion.model.dto;

import aphelion.model.domain.BlogManagersVisibilityLevel;
import aphelion.model.domain.BlogPostPublisherType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Create blog request", description = "Object containing data about created blog")
public class CreateBlogDTO {
    @NotNull
    @Size(max = 50)
    @ApiModelProperty(value = "Blog name", notes = "Max length is 50", required = true)
    private String name;

    @Nullable
    @Size(max = 100)
    @ApiModelProperty(value = "Blog description", notes = "Max length is 100")
    private String description;

    @ApiModelProperty(value = "Link to blog's avatar", notes = "Optional")
    private String avatarUri;

    @NotNull
    @ApiModelProperty(value = "Default publisher type", required = true)
    private BlogPostPublisherType defaultPublisherType;

    @NotNull
    @ApiModelProperty(value = "Blog managers vilibility level", required = true)
    private BlogManagersVisibilityLevel blogManagersVisibilityLevel;
}
