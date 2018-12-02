package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatedNumberOfBlogPostLikesDTO {
    private Long blogPostLikeId;
    private Integer updatedNumberOfLikes;
}
