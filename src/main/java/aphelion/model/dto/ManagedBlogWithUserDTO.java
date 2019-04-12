package aphelion.model.dto;

import aphelion.model.domain.BlogRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(
        value = "Managed blog with user",
        description = "Represents managed blog with user specified"
)
public class ManagedBlogWithUserDTO {
    @ApiModelProperty("Id of blog manager")
    private Long blogManagerId;
    @ApiModelProperty("Id of blog")
    private Long blogId;
    @ApiModelProperty("Role of blog manager")
    private BlogRole blogRole;
    @ApiModelProperty("User who manages the blog")
    private UserMinifiedDTO user;
}
