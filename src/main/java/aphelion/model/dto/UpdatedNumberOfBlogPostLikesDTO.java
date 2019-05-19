package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(
        value = "Updated number of blog post likes",
        description = "Object returned when blog post like is created or deleted.\n" +
                "When blog post like is created, then <code>blogPostLikeId</code> property " +
                "of returned object is equal to id assigned to created blog post.\n" +
                "When blog post like is deleted, then <code>blogPostLikeId</code> property " +
                "is <code>null</code>.\n" +
                "<code>updatedNumberOfLikes</code> property always contains current number of likes."
)
public class UpdatedNumberOfBlogPostLikesDTO {
    @ApiModelProperty("Id of blog post like")
    private Long blogPostLikeId;
    @ApiModelProperty("Updated number of likes")
    private Integer updatedNumberOfLikes;
}
