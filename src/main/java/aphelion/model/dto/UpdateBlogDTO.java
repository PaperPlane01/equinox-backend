package aphelion.model.dto;

import aphelion.model.domain.BlogManagersVisibilityLevel;
import aphelion.model.domain.BlogPostPublisherType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Update blog request", description = "Request to update blog")
public class UpdateBlogDTO {
    @NotBlank
    @Size(max = 50)
    @ApiModelProperty(
            value = "New name of blog",
            notes = "Max length is 50",
            required = true
    )
    private String name;

    @Nullable
    @Size(max = 100)
    @ApiModelProperty(
            value = "New description of blog",
            notes = "Max length is 100"
    )
    private String description;

    @NotNull
    @ApiModelProperty(value = "New blog managers visibility level", required = true)
    private BlogManagersVisibilityLevel blogManagersVisibilityLevel;

    @NotNull
    @ApiModelProperty(value = "New default publisher type", required = true)
    private BlogPostPublisherType defaultPublisherType;

    @ApiModelProperty(value = "New avatar uri")
    private String avatarUri;
}
