package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@ApiModel("Blog post like")
public class BlogPostLikeDTO {
    @ApiModelProperty("Id of like")
    private Long id;
    @ApiModelProperty("Id of blog post")
    private Long blogPostId;
    @ApiModelProperty("User who liked the blog post")
    private UserMinifiedDTO user;
}
