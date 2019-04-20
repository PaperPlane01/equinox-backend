package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
        value = "Create blog post like request",
        description = "Object used to create new blog post like"
)
public class CreateBlogPostLikeDTO {
    @ApiModelProperty("Id of blog post which is to be liked")
    @NotNull
    private Long blogPostId;
}
